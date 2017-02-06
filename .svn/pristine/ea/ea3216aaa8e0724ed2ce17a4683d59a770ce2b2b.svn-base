package com.millenium.credibanco.evaq.engine;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.FastMath;

import com.millenium.credibanco.evaq.exception.MicroCredibancoException;
import com.millenium.credibanco.evaq.model.LlaveValor;
import com.millenium.credibanco.evaq.model.ParametrosEvaluadorQ;
import com.millenium.credibanco.evaq.model.RespuestaEvaluador;
import com.millenium.credibanco.evaq.model.Tx;
import com.millenium.credibanco.evaq.model.ValoresEvaluador;

public class EvaluadorAdquirienteCincoQ {

	public static RespuestaEvaluador ejecutarEvaluador(List<Tx> listHistorico, Tx alerta, List<ParametrosEvaluadorQ> listParametrosEvaluadorQ) {

		listHistorico = listHistorico.stream().filter( tx -> tx.getFechaTx().isBefore( alerta.getFechaTx() ) ).collect( Collectors.toList() );
		
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

			String parametros=""
					+ "\n================================"
					+ "\n      ParametrosEvaluadorQ"
					+ "\n================================"
					+ "\nNombre: " + parametroEvaluador.getId()+"-"+parametroEvaluador.getNombre()
					+ "\nTipo: " + parametroEvaluador.getTipo()
					+ "\nTipoOrigen: " + parametroEvaluador.getTipoOrigen()
					+ "\nRevisión: " + parametroEvaluador.getTipoRevision()
					+ "\nNacional: " + (parametroEvaluador.getNacional()?"SI":"NO")
					+ "\nPresencial: " + (parametroEvaluador.getPresencial()?"SI":"NO")
					+ "\nDescripción: " + parametroEvaluador.getDescripcion()
					+ "\nPonderación: " + parametroEvaluador.getPonderacion()
					+ "";
			System.out.println( parametros );
			System.out.println( alerta.toString() );
			if( parametroEvaluador.getTipo().indexOf( "Q" )!=-1 ){
				try{
					LlaveValor lv = evaluarQs( listHistorico, alerta, parametroEvaluador );
					lv.setPonderacion( parametroEvaluador.getPonderacion() );
					listRespuestaEvaluador.add( lv );
//					listRespuestaEvaluador.add( evaluarQs( listHistorico, alerta, parametroEvaluador ) );
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

		System.out.println("Id Alerta: " + alerta.getId());
		for (LlaveValor llaveValor : listRespuestaEvaluador) {
			System.out.println("Nombre Q : " + llaveValor.getLlave() + "  Valor q: " + llaveValor.getValor() + " Ponderación: " + llaveValor.getPonderacion() );
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
			
			System.out.println( "Id Alerta: " + alerta.getId() + " suma ponderación: " + suma  ); 
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
//
//				if (!r.getRiesgo().equals("B") || !r.getRiesgo().equals("MB") ) {
//					bajo = false;
//				}
//			}
//
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

	private static LlaveValor evaluarQs(List<Tx> listHistorico, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		
		if (parametroEvaluador.getTipo().equals("Q1")) {
			return (evaluadorQ1(listHistorico, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q2")) {
			return(evaluadorQ2(listHistorico, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q3")) {
			return(evaluadorQ3(listHistorico, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q4")) {
			return(evaluadorQ4(listHistorico, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q5")) {
			return(evaluadorQ5(listHistorico, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q6")) {
			return(evaluadorQ6(listHistorico, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q7")) {
			return(evaluadorQ7(listHistorico, alerta, parametroEvaluador));

		}
		if (parametroEvaluador.getTipo().equals("Q8")) {
			return(evaluadorQ8(listHistorico, alerta, parametroEvaluador));

		}

		System.out.println( "Tipo "+ parametroEvaluador.getTipo() + " NO programada"  );
		return null;
	}


	private static LlaveValor evaluadorQ8(List<Tx> listHistorico, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		// la cantidad de fraudes vs. Las aprobaciones.
		// socre = fraudes/transacciones * 100, fraudes = SUM( tx_fraude_si  ), transacciones=sum(tx)

		if( listHistorico.size()==0 ){
			throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con histórico insuficiente" );
		}
		
		Map<String, Long> mapFraude = listHistorico.stream()
				.filter( tx-> tx.getFechaTx().isBefore( alerta.getFechaTx() )  )
				.collect( Collectors.groupingBy( tx -> ( ( ( tx.getEstadoTrx()!=null && tx.getEstadoTrx().equals("FRAUDE"))?"SI": "NO" ) ) , Collectors.counting() ) );

		long txFraudeSi = (mapFraude.get("SI")!=null?mapFraude.get("SI"):0l);
		long txTotal = (mapFraude.get("NO")!=null?mapFraude.get("NO"):0) + txFraudeSi ;
		long score =-1L;
		if( txTotal > 0 ){
			score = ( txFraudeSi/txTotal )*100;
		}else{
			throw new MicroCredibancoException( "Alerta " + alerta.getId() + ", no se pudo determinar cantidad de Fraudes (trxTotal = 0)" );
		}
		return rango(parametroEvaluador, score);
	}

	private static LlaveValor evaluadorQ7(List<Tx> listHistorico, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		// JOIN TABLA CON VALORES POSCONDITION
		// TODO alerta.getCodigocondicionpos() : debe existir un equivalente con Nivel de Riesgo
		
		if(  parametroEvaluador.getListValores().size()==0 ){
			throw new MicroCredibancoException( "ParametrosEvaluador " + parametroEvaluador.getId() + " con valores indeterminados" );
		}
		
		String pc = alerta.getCodigocondicionpos();
		if( pc==null || pc.trim().isEmpty()){
			throw new MicroCredibancoException(  "Alerta " + alerta.getId() + " con PC indeterminado o null");
		}
		long score = -1;
		for( ValoresEvaluador val : parametroEvaluador.getListValores() ){
			if( val.getValor().equals( pc ) ){
				score= Long.valueOf( val.getRiesgo() );
			}
		}
		if( score==-1 ){
			throw new MicroCredibancoException(  "Alerta " + alerta.getId() + " con PC no tipificado: " + pc);
		}

		return rango(parametroEvaluador, score);

	}

	private static LlaveValor evaluadorQ6( List<Tx> listHistorico, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		// Cantidad de transacciones en el día supera el promedio día que presenta el comercio
		// score=( nTx_hoy - media / std ), nTx_hoy=sum( TX hoy ), media=med( tx por cada día )

		if( listHistorico.size()==0 ){
			throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con histórico insuficiente" );
		}
		
		LocalDateTime dateCheck = alerta.getFechaTx().truncatedTo( ChronoUnit.DAYS );
		Long nTx = listHistorico.stream()
				.filter( tx-> tx.getFechaTx().truncatedTo( ChronoUnit.DAYS ).equals( dateCheck ) )
				.collect( Collectors.counting() ) + 1;

		double[] nTxHistorico = listHistorico.stream()
				.filter( tx-> tx.getFechaTx().truncatedTo( ChronoUnit.DAYS ).isBefore( dateCheck ) )
				.collect( Collectors.groupingBy( tx -> tx.getFechaTx().truncatedTo( ChronoUnit.DAYS ) , Collectors.counting() ) )
				.values().stream().mapToDouble(d -> d).toArray();

		double mean = StatUtils.mean( nTxHistorico );
		double std = FastMath.sqrt(StatUtils.variance( nTxHistorico ));
		long score = 100L;
		
		if( Double.isNaN( mean ) ){
			mean = 0;
			std = 0;
		}
		if( std==0 ){
			std=1;
		}
		score = Math.abs(Math.round( (nTx- mean )/std ));

		return rango(parametroEvaluador, score);

	}

	private static LlaveValor evaluadorQ5(List<Tx> listHistorico, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		// El monto de la trx alertada se encuentran en el promedio de facturación del comercio en los últimos seis meses. (Tomar en cuenta desde el mes 2 al 6)
		// score=abs( alerta_monto-media / std ), media=med( tx_monto ), tx_monto>0 , 6 meses < tx < 1 meses
		LocalDateTime dateCheckBotton = alerta.getFechaTx().minusMonths(6);
		LocalDateTime dateCheckTop = alerta.getFechaTx().minusMonths(1);
		
		if( listHistorico.size()==0 ){
			throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con histórico insuficiente" );
		}

		double[] montos = listHistorico.stream()
				.filter( tx -> ( tx.getMontotx()>0 && tx.getFechaTx().isAfter( dateCheckBotton ) && tx.getFechaTx().isBefore( dateCheckTop )) )
				.mapToDouble( tx -> tx.getMontotx() )
				.toArray();

		double mean = StatUtils.mean( montos );
		double std = FastMath.sqrt(StatUtils.variance( montos ));
		long score = 100L;
		
		if( Double.isNaN( mean ) ){
			mean = 0;
			std = 0;
		}
		if( std==0 ){
			std= 1;
		}
		
		score = Math.abs( Math.round( (alerta.getMontotx()- mean )/std ) );

		return rango(parametroEvaluador, score);

	}

	private static LlaveValor evaluadorQ4(List<Tx> listHistorico, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		// saca la 6 transaccion del arreglo ordenado por fecha de mayor a menor
		final Comparator<Tx> comp = (p1, p2) -> (p2.getFechaTx().compareTo(p1.getFechaTx()));
		
		if( listHistorico.size()<=5 ){
			throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con histórico insuficiente" );	
		}
		
		Tx tx3th = null;
		if( listHistorico.size()<=5 ){
			tx3th = listHistorico.stream().sorted(comp).limit( listHistorico.size()-1 ).reduce((a, b) -> b).get();	
		}else{
			tx3th = listHistorico.stream().sorted(comp).limit(5).reduce((a, b) -> b).get();
		}
		long score = ChronoUnit.MINUTES.between( tx3th.getFechaTx(), alerta.getFechaTx() );
		return rango(parametroEvaluador, score);
	}

	private static LlaveValor evaluadorQ3(/* List<Tx> results, Tx alerta */List<Tx> listHistorico, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		// Antigüedad del comercio respecto a la fecha de afiliación? (tomar dato de AS 400)
		// meses= ( alerta_fecha_afiliacion - alerta_fecha )
		
		if( alerta.getFechaAfiliacion()==null ){
			throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con fechaAfiliación indeterminada" );	
		}
		
		long score = ( ChronoUnit.DAYS.between( alerta.getFechaAfiliacion()  , alerta.getFechaTx() )) / 30;
		return rango(parametroEvaluador, score);

	}

	private static LlaveValor evaluadorQ2(List<Tx> listHistorico, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		// Concentración de uso de tarjetas de un mismo banco en los últimos tres meses
		// score= (max_fiid/total)*100, maxfiid=MAX( Tx_agrupadas by fiid ), total=SUM(Tx), 3meses<tx.fecha<alerta
		LocalDateTime dateCheck = alerta.getFechaTx().minusMonths(3);
		
		if( listHistorico.size()==0 ){
			throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con histórico insuficiente" );	
		}

		Map<String, Long> mapResult = listHistorico.stream()
				.filter( tx -> ( tx.getFiid()!=null && tx.getFechaTx().isAfter( dateCheck )) )
				.collect( Collectors.groupingBy( Tx::getFiid , Collectors.counting() ) );

		long max_fiid = mapResult.values().stream()
				.max( Long::compareTo ).get();
		long total = mapResult.values().stream().mapToLong(l->l).sum();

		long score = 100L;
		if( total>0 ){
			score = (max_fiid/total)*100;
		}

		return rango(parametroEvaluador, score);
	}

	// TODO los datos no deben estar quemados
	private static List<String> aprobadaCode = Arrays.asList( "000","001","002","003","004","005","006","007","008","009" );
	private static LlaveValor evaluadorQ1(List<Tx> listHistorico, Tx alerta, ParametrosEvaluadorQ parametroEvaluador) throws MicroCredibancoException {
		// la cantidad de negaciones vs. Las aprobaciones.
		// score = (negacioens/transacciones)*100 , negaciones=sum(Tx) donde Tx=NEGADA & Tx_fecha<Alarma, Transacciones= SUM(Tx)

		if( alerta.getReponseCode()==null){
			throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con ResponseCode indeterminado" );
		}
		
		if( listHistorico.size()==0 ){
			throw new MicroCredibancoException( "Alerta " + alerta.getId() + " con histórico insuficiente" );	
		}

		Map<String, Long> mapResult = listHistorico.stream()
				.filter( tx-> tx.getFechaTx().isBefore( alerta.getFechaTx() )  )
				.collect( Collectors.groupingBy( tx -> ( (aprobadaCode.contains( tx.getReponseCode() )?"APROBADA": "NEGADA" ) ) , Collectors.counting() ) );

		long txNegadas = mapResult.get("NEGADA");
		long txTotal = mapResult.get("APROBADA") + txNegadas ;
		long score =-1L;
		if( txTotal > 0 ){
			score = (txNegadas/txTotal)*100;
		}
		return rango(parametroEvaluador, score);

	}

	public static LlaveValor rango(ParametrosEvaluadorQ parametroEvaluador, long score) throws MicroCredibancoException {

		LlaveValor q = new LlaveValor();

		String valorq = "-1";
		String riesgo = "-1";

		try{
			if (score >= parametroEvaluador.getRangoMinMuyAlto() && score <= parametroEvaluador.getRangoMaxMuyAlto()) {
	
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
	
			} else if (score >= parametroEvaluador.getRangoMinMuyBajo() && score <= parametroEvaluador.getRangoMaxMuyBajo()) {
	
				valorq = parametroEvaluador.getMuyBajo().toString();
				riesgo = "MB";
	
			}
		}catch( Exception e ){
//			throw new MicroCredibancoException("Evaluando Rango: "+ parametroEvaluador.getId( )+":"+score + " , " +e.getMessage() );
			throw new MicroCredibancoException("Score en rango desconocido: id_parametro:"+ parametroEvaluador.getId( )+" valor:"+score + " , " +e.getMessage() );
		}

		q.setLlave(parametroEvaluador.getTipo());
		q.setValor(valorq);
		q.setRiesgo(riesgo);

		return q;

	}
}