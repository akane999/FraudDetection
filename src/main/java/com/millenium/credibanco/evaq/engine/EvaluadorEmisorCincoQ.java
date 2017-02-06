package com.millenium.credibanco.evaq.engine;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.FastMath;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.millenium.credibanco.evaq.exception.MicroCredibancoException;
import com.millenium.credibanco.evaq.model.LlaveValor;
import com.millenium.credibanco.evaq.model.ParametrosEvaluadorQ;
import com.millenium.credibanco.evaq.model.RespuestaEvaluador;
import com.millenium.credibanco.evaq.model.Tx;
import com.millenium.credibanco.evaq.util.ServiceCredibancoUtil;

public class EvaluadorEmisorCincoQ {

	public static RespuestaEvaluador ejecutarEvaluador(List<Tx> listHistoricoSinalerta, Tx alerta, List<ParametrosEvaluadorQ> listParametrosEvaluadorQ) {

		listHistoricoSinalerta = listHistoricoSinalerta.stream().filter( tx -> tx.getFechaTx().isBefore( alerta.getFechaTx() ) ).collect( Collectors.toList() );
		
		if( alerta.getNombreAplicacion().equals( "VRM" ) && ( alerta.getPortafolio().equals( "12" ) || alerta.getPortafolio().equals( "17" ) ) ){
			alerta.setFechaTx( alerta.getFechaTx().minusHours( -5l ) );
		}
		
		if( alerta.getNombreAplicacion().equals("VRM") && alerta.getPortafolio().equals( "17" )  ){
			int USD_COP = Integer.valueOf( System.getProperty("evaluador.usd_cop", "3000") );
			alerta.setMontotx( alerta.getMontotx()*USD_COP );
		}
		
		System.out.println( "===ALERTA A EVALUAR: id " + alerta.getId() + " con " + listParametrosEvaluadorQ.size() +" ParametrosEvaluador===" );
		System.out.println( alerta.toString() );
		
		boolean isError=false;
		List<LlaveValor> listRespuestaEvaluador = new ArrayList<LlaveValor>();
		for (ParametrosEvaluadorQ parametroEvaluador : listParametrosEvaluadorQ) {

			ServiceCredibancoUtil.printParametrosEvaluador(parametroEvaluador, "CincoQ" );
			
			if( parametroEvaluador.getTipo().indexOf( "Q" )!=-1 ){
				try{					
					LlaveValor lv = evaluarQs( listHistoricoSinalerta, alerta, parametroEvaluador );
					lv.setPonderacion( parametroEvaluador.getPonderacion() );
					listRespuestaEvaluador.add( lv );
				}catch( Exception e ){
					System.out.println( "SE DETECTA UNA EXCEPCIÓN" );
					e.printStackTrace();
					LlaveValor q = new LlaveValor( parametroEvaluador.getTipoOrigen(), "-1", "N/A" );
					q.setDetalleError( e.getMessage() );
					listRespuestaEvaluador.add( q );
					isError=true;
				}
			}else{
				System.out.println( parametroEvaluador.getTipo()+" "+ "No implementado" );
			}

		}
		
		System.out.println("Id Alerta: " +  alerta.getId() );
		for (LlaveValor llaveValor : listRespuestaEvaluador) {
			System.out.println("Nombre Q : " + llaveValor.getLlave() + "  Valor q: " + llaveValor.getValor());
		}

		RespuestaEvaluador respuestaEvaluador = new RespuestaEvaluador();
		respuestaEvaluador.setIdAlerta(alerta.getId());

		if( !isError ){
			
			// ponderación
			double suma = 0;
			for ( LlaveValor  r : listRespuestaEvaluador) {
				if( r.getPonderacion()!=null ){
					double valorPonderado = Integer.valueOf( r.getValor() ) * r.getPonderacion();
					suma = suma + valorPonderado;
					r.setValorPonderado( "" + valorPonderado );
				}
			}
			
			String nivelRiesgo="-1";
			if( suma>75 ){
				nivelRiesgo = "ALTO";
			}else if( suma>50 ){
				nivelRiesgo = "MEDIO";
			}else{
				nivelRiesgo = "BAJO";
			}
			
			
			// votación
//			boolean alto = false;
//			boolean bajo = true;
//			for ( LlaveValor  r : listRespuestaEvaluador) {
//				if (r.getRiesgo().equals("MA") || r.getRiesgo().equals("A") ) {
//					alto = true;
//					break;
//				}
//				if (!r.getRiesgo().equals("B") || !r.getRiesgo().equals("MB") ) {
//					bajo = false;
//				}
//			}
//			String nivelRiesgo="-1";
//			if (alto) {
//				nivelRiesgo="ALTO";
//			} else if (bajo) {
//				nivelRiesgo="BAJO";
//			} else {
//				nivelRiesgo="MEDIO";
//			}

			respuestaEvaluador.setNivelRiesgo( nivelRiesgo );
		}else{
			respuestaEvaluador.setNivelRiesgo( "N/A" );
		}
		
		respuestaEvaluador.setResultadosQ( listRespuestaEvaluador );

		return respuestaEvaluador;
	}

	private static LlaveValor evaluarQs(List<Tx> listHistoricoSinalerta, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		if (parametroEvaluador.getTipo().equals("Q1")) {
			return (evaluadorQ1(listHistoricoSinalerta, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q2")) {
			return(evaluadorQ2(listHistoricoSinalerta, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q3")) {
			return(evaluadorQ3(listHistoricoSinalerta, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q4")) {
			return(evaluadorQ4(listHistoricoSinalerta, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q5")) {
			return(evaluadorQ5(listHistoricoSinalerta, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q6")) {
			return(evaluadorQ6(listHistoricoSinalerta, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q7")) {
			return(evaluadorQ7(listHistoricoSinalerta, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q8")) {
			return(evaluadorQ8(listHistoricoSinalerta, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q9")) {
			return(evaluadorQ9(listHistoricoSinalerta, alerta, parametroEvaluador));

		}

		System.out.println( "Tipo "+ parametroEvaluador.getTipo() + " NO programada"  );
		return null;
	}
	

	private static LlaveValor evaluadorQ9(List<Tx> listHistoricoSinalerta, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		// Frecuencia promedio mensual de uso del canal de la alerta, en los últimos 6 meses
		// score = avg( cantidad_mes[] ),  cantidad_mes=SUM( tx by Mes ) donde tx.fechaTx > 6 meses y tx.ps = alerta.ps
		
		String ps = alerta.getCodigocondicionpos() ;
		long score =0;
		if( ps==null || ps.trim().isEmpty() ){
			score=1l;
		}else{
			// Resticciones del historial: fecha no mayor a 6 meses e igual pscondition
			final List<MicroCredibancoException> excepcionList = new ArrayList<MicroCredibancoException>();
			LocalDateTime dateCheck= alerta.getFechaTx().minusMonths(6);
			double[] cantidad_mes = listHistoricoSinalerta.stream()
					.filter( tx -> 
					{
						if( tx.getCodigocondicionpos()==null || tx.getCodigocondicionpos().trim().isEmpty() ){
							excepcionList.add( new MicroCredibancoException("Historico "+ tx.getId() +" con PC indeterminado") );
							return false;
						}
						return ( tx.getCodigocondicionpos().equals( ps ) &&  tx.getFechaTx().isAfter( dateCheck ) ); 
					})
					.collect( Collectors.groupingBy( tx -> tx.getFechaTx().getMonth() , Collectors.counting() ) )
					.values().stream().mapToDouble(d -> d).toArray();
			
			if( excepcionList.size()>0 ){
				throw excepcionList.get( 0 );
			}
	
			double mean = StatUtils.mean( cantidad_mes );
			if( Double.isNaN( mean ) ){
				mean = 0;
			}
			score = Math.round( mean );
		}
	
		System.out.println("score " + score);
		return rango(parametroEvaluador, score);
	}

	private static LlaveValor evaluadorQ8(List<Tx> listHistoricoSinalerta, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		// Modo de entrada	Nivel de Riesgo	Escala Riesgo
		// score = alerta_poscondicion
		String ps = alerta.getCodigocondicionpos() ;
		long score = -1;		
		if( ps==null || ps.trim().isEmpty() ){
			score=100l;
		}else{
			switch ( ps ) {
			case "00":
				score=100;
				break;
			case "59":
				score=90;
				break;
			case "51":
				score=90;
				break;
			case "08":
				score=80;
				break;
			case "01":
				score=20; //-> MEDIO 
				break;
			default:
				score = 0;
				throw new MicroCredibancoException( "Alerta "+ alerta.getId()  +" con PC no tipificado: " + ps );
			}
		}
		
		System.out.println("score " + score);
		return rango(parametroEvaluador, score);
	}

	private static LlaveValor evaluadorQ7(List<Tx> listHistoricoSinalerta, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		
		boolean isMcc=false;
		if(	(alerta.getIdComercio()==null || alerta.getIdComercio().equals("null")
				|| alerta.getIdComercio().equals("-1") ) || alerta.getIdComercio().equals("vacio") ){
			if( alerta.getMcc()==null || alerta.getMcc().equals( "vacio" ) || alerta.getMcc().equals( "null" ) ){
				throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con identificador y MCC de comercio indeterminado" );	
			}else{
				isMcc=true;
			}
		}

		final boolean isAlertaMcc = isMcc ;
		
		final List<MicroCredibancoException> excepcionList = new ArrayList<MicroCredibancoException>();
		Map<String, List<Tx>> difftrx = listHistoricoSinalerta.stream()
				.filter(tx -> {
					boolean isHistoricoMcc = false;			
					if(	(tx.getIdComercio()==null || tx.getIdComercio().equals("null")
							|| tx.getIdComercio().equals("-1") ) || tx.getIdComercio().equals("vacio") ){
						if( tx.getMcc()==null || tx.getMcc().equals( "vacio" ) || tx.getMcc().equals( "null" ) ){
							excepcionList.add( new MicroCredibancoException( "Historico " + tx.getId() + " con identificador y MCC de comercio indeterminado" ) );
							return false;
						}else{
							isHistoricoMcc=true;
						}
					}				
					
					if(!isAlertaMcc && !isHistoricoMcc ){							
						if (!tx.getIdComercio().equals(alerta.getIdComercio()) ){
							return true;
						}else{
							return false;
						}
					}else if( !isAlertaMcc && isHistoricoMcc ){
						if( alerta.getMcc()==null || alerta.getMcc().equals( "-1" )
								|| alerta.getMcc().equals( "null" ) || alerta.getMcc().equals( "vacio" ) ){
							excepcionList.add( new MicroCredibancoException("Historico "+ tx.getId() +" sin idComercio, se evalúa MCC, pero Alerta"+ alerta.getId() +"sin MCC") );
							return false;
						}
						if (!tx.getMcc().equals(alerta.getMcc()) ){
							return true;
						}else{
							return false;
						}
					}else if( isAlertaMcc ){
						if( tx.getMcc()==null || tx.getMcc().equals( "-1" )
								|| tx.getMcc().equals( "null" ) || tx.getMcc().equals( "vacio" ) ){
							excepcionList.add( new MicroCredibancoException("Alerta"+ alerta.getId() +" sin idComercio, se evalua MCC, pero Historico "+ tx.getId() +" sin MCC") );
							return false;
						}
						if (!tx.getMcc().equals(alerta.getMcc()) ){
							return true;
						}else{
							return false;
						}
					}else{
						return false;
					}
		}).collect(Collectors.groupingBy(Tx::getIdComercio) ) ;

		if( excepcionList.size()>0 ){
			throw excepcionList.get( 0 );
		}

		List<LocalDateTime> orderedtx = new ArrayList<LocalDateTime>();

		for (String comercio : difftrx.keySet()) {
			orderedtx.add(difftrx.get(comercio).stream().map(Tx::getFechaTx).max(LocalDateTime::compareTo).get());
//			System.out.println("comercio tamaño " + orderedtx + " idComercio" + comercio);
		}

		long score = -1L;
		System.out.println("comercio tamaño " + orderedtx.size() + " id alerta " + alerta.getFechaTx());
		
		orderedtx = Lists.reverse(orderedtx);
		if (orderedtx.size() == 0 ) {
			score = 0;
		}else if (orderedtx.size() <= 3) {
			score = Math.abs( ChronoUnit.MINUTES.between(orderedtx.get( orderedtx.size()-1 ), alerta.getFechaTx()) );
		} else {
			score = Math.abs( ChronoUnit.MINUTES.between(orderedtx.get( 3 ), alerta.getFechaTx()) );
		}
		System.out.println("score " + score);

		return rango(parametroEvaluador, score);

	}

	private static LlaveValor evaluadorQ6(List<Tx> listHistoricoSinalerta, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		// '80' then '100'
		// '02' then '90'
		// '90' then '60'
		// '05' then '20'
		
		String me = alerta.getModoentrada();
		long score = 100l;
		if( me==null ){
			score = 100l;
		}else{
			String modoentrada = ( me.length()>=2? me.substring(0, 2) : me ) ;
			
			switch ( modoentrada ) {
			case "80":
				score=100;
				break;
			case "02":
				score=90;
				break;
			case "90":
				score=60;
				break;
			case "05":
				score=20;
				break;
			case "01":
				score=20;
				break;
			default:
				score = 0;
				throw new MicroCredibancoException( "Alerta "+ alerta.getId()  +" con Modo de Entrada no tipificado: " + me);
			}
		}
		
		return rango( parametroEvaluador , score );
		

//		LlaveValor q = new LlaveValor();
//
//		String valorq = "0";
//		String modoentrada = alerta.getModoentrada();
//		System.out.println("Modo de entrda: " + modoentrada);
//
//		List<ValoresEvaluador> listValoresMuyAlto = parametroEvaluador.getListValorMuyAlto();
//		List<ValoresEvaluador> listValoresAlto = parametroEvaluador.getListValorAlto();
//		List<ValoresEvaluador> listValoresMedio = parametroEvaluador.getListValorMedio();
//		List<ValoresEvaluador> listValoresBajo = parametroEvaluador.getListValorBajo();
//		List<ValoresEvaluador> listValoresMuyBajo = parametroEvaluador.getListValorMuyBajo();
//
//		boolean valorEncontrado = false;
//
//		if (!valorEncontrado) {
//
//			for (ValoresEvaluador valorMuyAlto : listValoresMuyAlto) {
//				if (modoentrada.equals(valorMuyAlto.getValor())) {
//					valorq = parametroEvaluador.getMuyAlto().toString();
//					valorEncontrado = true;
//				}
//			}
//		}
//
//		if (!valorEncontrado) {
//			for (ValoresEvaluador valorAlto : listValoresAlto) {
//				if (modoentrada.equals(valorAlto.getValor())) {
//					valorq = parametroEvaluador.getAlto().toString();
//					valorEncontrado = true;
//				}
//			}
//		}
//
//		if (!valorEncontrado) {
//			for (ValoresEvaluador valorMedio : listValoresMedio) {
//				if (modoentrada.equals(valorMedio.getValor())) {
//					valorq = parametroEvaluador.getMedio().toString();
//					valorEncontrado = true;
//
//				}
//			}
//		}
//
//		if (!valorEncontrado) {
//			for (ValoresEvaluador valorBajo : listValoresBajo) {
//				if (modoentrada.equals(valorBajo.getValor())) {
//					valorq = parametroEvaluador.getBajo().toString();
//					valorEncontrado = true;
//
//				}
//			}
//		}
//
//		if (!valorEncontrado) {
//			for (ValoresEvaluador valorMuyBajo : listValoresMuyBajo) {
//				if (modoentrada.equals(valorMuyBajo.getValor())) {
//					valorq = parametroEvaluador.getMuyBajo().toString();
//					valorEncontrado = true;
//
//				}
//			}
//		}
//
//		System.out.println("valor q: " + valorq);
//
//		q.setLlave(parametroEvaluador.getTipo());
//		q.setValor(valorq);
//
//		return q;

	}

	private static LlaveValor evaluadorQ5(List<Tx> historico, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		
		final Comparator<Tx> comp = (p1, p2) -> (p2.getFechaTx().compareTo(p1.getFechaTx()));
		
		if( historico.size()<=2 ){
			throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con histórico insuficiente" );	
		}
		
		// saca la 3 transaccion del arreglo ordenado por fecha de mayor a menor
		Tx tx3th = null;
		if( historico.size()<=3 ){
			tx3th = historico.get( historico.size()-1 );
		}else{
			tx3th = historico.stream().sorted(comp).limit(3).reduce((a, b) -> b).get();
		}
		System.out.println("fecha cuarta trx " + tx3th.getFechaTx());
		long score = Math.abs( ChronoUnit.MINUTES.between( tx3th.getFechaTx(), alerta.getFechaTx()) );

		return rango(parametroEvaluador, score);

	}

	private static LlaveValor evaluadorQ4(List<Tx> listHistoricoSinalerta, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		long score = 1l;
		if( alerta.getUbicacionTrx()==null || alerta.getUbicacionTrx().equals( "null" )
				|| alerta.getUbicacionTrx().equals( "vacio" ) || alerta.getUbicacionTrx().equals( "-1" ) ){
			score = 1l;
		}else{

			final Comparator<Tx> comp = (p1, p2) -> (p1.getFechaTx().compareTo(p2.getFechaTx()));
			Stream<Tx> txStream = listHistoricoSinalerta.stream();
			final List<MicroCredibancoException> excepcionList = new ArrayList<MicroCredibancoException>();
			Optional<Tx> nextcityOptional = txStream.filter(tx ->{
				if( tx.getUbicacionTrx().equals( "vacio" ) || tx.getUbicacionTrx().equals( "-1" ) ){
					excepcionList.add( new MicroCredibancoException("Historia "+ tx.getId() +" con UBICACIÓN indeterminada") );
					return false;
				}				
				if( !tx.getUbicacionTrx().equals(alerta.getUbicacionTrx()) ){
					return true;
				}else{
					return false;
				}
				
			}).max(comp);
			
			if( excepcionList.size()>0 ){
				throw excepcionList.get(0) ;
			}
			
			Tx nextcity = null;
			if( nextcityOptional.isPresent() ){
				nextcity = nextcityOptional.get();
				score = Math.abs( Math.round( ChronoUnit.MINUTES.between(nextcity.getFechaTx(), alerta.getFechaTx()) ) );
			}
		}

		return rango(parametroEvaluador, score);
	}

	private static LlaveValor evaluadorQ3(List<Tx> listHistoricoSinalerta, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {

		Long trxdia = listHistoricoSinalerta.stream().filter(t -> t.getDaytx().equals( alerta.getDaytx()) ).collect(Collectors.counting());

		double[] trxbyDay = listHistoricoSinalerta.stream().filter( tx -> !tx.getDaytx().equals( alerta.getDaytx()) ).collect(Collectors.groupingBy(Tx::getDaytx, Collectors.counting())).values().stream().mapToDouble(d -> d).toArray();
		double mean = StatUtils.mean(trxbyDay);
		double std = FastMath.sqrt(StatUtils.variance(trxbyDay));
		
		long score = -1L;
		if( Double.isNaN( mean ) ){
			mean = 0;
			std = 0;
		}
		if( std==0 ){
			std = 1;
		}
		score = Math.abs( Math.round((trxdia - mean) / std) );

//		System.out.println("transacciones por dia " + mean + " std " + std + " score " + score);

		return rango(parametroEvaluador, score);

	}

	private static LlaveValor evaluadorQ2(List<Tx> listHistoricoSinalerta, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException{
		
		Predicate<Double> validMonto = new Predicate<Double>() {
			public boolean apply(Double valor) {
				return valor > 0d;
			}
		};

		@SuppressWarnings("unchecked")
		Collection<Double> allmontos = CollectionUtils.collect(listHistoricoSinalerta, TransformerUtils.invokerTransformer("getMontotx"));
		Iterable<Double> montos = Iterables.filter(allmontos, validMonto);
		Double[] ds = Iterables.toArray(montos, Double.class);
		double[] d = ArrayUtils.toPrimitive(ds);
		double mean = StatUtils.mean(d);
		double std = FastMath.sqrt(StatUtils.variance(d));
		long score = -1L;
		
		if( Double.isNaN( mean ) ){
			mean = 0;
			std = 0;
		}
		if( std<=0 ){
			std=1;
		}
			
		score = Math.abs( Math.round((alerta.getMontotx() - mean) / std) );
		
		return rango(parametroEvaluador, score);
	}

	// para buscar cuantas veces ocurrio la transaccion
	private static LlaveValor evaluadorQ1(List<Tx> listHistoricoSinalerta, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		
		if( listHistoricoSinalerta.size()==0 ){
			throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con histórico insuficiente" );	
		}
		
		boolean isMcc=false;
		if(	(alerta.getIdComercio()==null || alerta.getIdComercio().equals("null")
				|| alerta.getIdComercio().equals("-1") ) || alerta.getIdComercio().equals("vacio") ){
			if( alerta.getMcc()==null || alerta.getMcc().equals( "vacio" ) || alerta.getMcc().equals( "null" ) ){
				throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con identificador y MCC de comercio indeterminado" );	
			}else{
				isMcc=true;
			}
		}
		
		List<Tx> output = new ArrayList<Tx>();
		Predicate<Tx> oneMonthBefore = new Predicate<Tx>() {
			public boolean apply(Tx tx) {
				return tx.getFechaTx().isBefore(alerta.getFechaTx().minusMonths(1));
			}
		};
		Iterable<Tx> result = Iterables.filter((Collection<Tx>) listHistoricoSinalerta, oneMonthBefore);
		for (Tx tx : result) {
			if( !isMcc ){
				if( tx.getIdComercio()!=null && tx.getIdComercio().equals(alerta.getIdComercio() ) ) {
					output.add(tx);
				}
			}else{
				if( tx.getMcc()!=null && tx.getMcc().equals(alerta.getMcc() ) ) {
					output.add(tx);
				}
			}

		}
		long score = output.size();

		return rango(parametroEvaluador, score);
	}
	

//	// para marcar cual trx del historico es la alerta
//	public static List<Tx> eliminarAlertaHistorico(Tx trx, HistoricoTx historico) {
//		// System.out.println("tamaño historico " +
//		// historico.getTransacciones().size());
//
//		List<Tx> results = new ArrayList<Tx>(historico.getTransacciones());
//		for (Tx tx : historico.getTransacciones()) {
//			if (((tx.getFechaTx().equals(trx.getFechaTx())) && (tx.getIdComercio().equals(trx.getIdComercio())) || (tx.getFechaTx().isAfter(trx.getFechaTx())))) {
//				System.out.println("resultado " + tx.getFechaTx().equals(trx.getFechaTx()));
//				results.remove(tx);
//
//			}
//
//		}
//
//		return results;
//	}

	public static LlaveValor rango(ParametrosEvaluadorQ parametroEvaluador, long score) throws MicroCredibancoException {

		LlaveValor q = new LlaveValor();

		String valorq = "-1";
		String riesgo = "-1";
		
		try{
			if ( score>=parametroEvaluador.getRangoMinMuyAlto() && score <= parametroEvaluador.getRangoMaxMuyAlto() ) {
				
				valorq = parametroEvaluador.getMuyAlto().toString();
				riesgo = "MA";
	
			} else if (score >= parametroEvaluador.getRangoMinAlto() && score <= parametroEvaluador.getRangoMaxAlto()) {
	
				valorq = parametroEvaluador.getAlto().toString();
				riesgo = "A";
	
			} else if (score >= parametroEvaluador.getRangoMinMedio() && score <= parametroEvaluador.getRangoMaxMedio()) {
	
				valorq = parametroEvaluador.getMedio().toString();
				riesgo = "M";
	
			} else if (score >= parametroEvaluador.getRangoMinBajo() && score <= parametroEvaluador.getRangoMaxBajo()) {
	
				valorq = parametroEvaluador.getBajo().toString();
				riesgo = "B";
	
			} else if ( score <= parametroEvaluador.getRangoMaxMuyBajo() ) {
				valorq = parametroEvaluador.getMuyBajo().toString();
				riesgo = "MB";
			}
			
			if( valorq.equals( "-1" ) && riesgo.equals( "-1" ) ){
				throw new Exception();
			}
			
		}catch( Exception e ){
			throw new MicroCredibancoException("Score en rango desconocido: id_parametro:"+ parametroEvaluador.getId( )+" valor:"+score + " , " +e.getMessage() );
		}

		q.setLlave(parametroEvaluador.getTipoOrigen() );
		q.setValor(valorq);
		q.setRiesgo(riesgo);

		return q;

	}
}