package sms_ApiRest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;



import com.google.gson.JsonParser;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;

public class app {
	
	//Declaramos las variiables de instancia 
	String numeroCel;
	String textoSms;

	
	// En el constructor de la clase Main inicializamos las variables de instancia numeroCel y textoSms
	public app(String numeroCel, String textoSms){  
			this.numeroCel = numeroCel; // Utilizamos la palabra clave "this"  para hacer referencia a las variables de instancia de la clase
			this.textoSms=textoSms;
	   } 	 


	public static void main(String[] args) {
		
		//Pasamos parametros directamente por el IDE y no por la consola
//		int xIdLocal = 101; 
//	    int xIdPeriodo = 202304; 
		
		 int xIdLocal = Integer.parseInt(args[0]);
		 int xIdPeriodo = Integer.parseInt(args[1]);
		    
		// Creamos una instancia de la clase AppThread y utilizamos los argumentos xIdLocal y xIdPeriodo para inicializar las variables
		AppThread appThread = new AppThread(xIdLocal, xIdPeriodo);

		// Creamos un objeto Thread y le pasamos la instancia de AppThread
		Thread hilo = new Thread(appThread);

		// Iniciamos la ejecución del hilo
		hilo.start();
		
		

		

	}
	
	public  void EnviaSms(String xNumeroCelular, String xTextoSMS) {
		
		// Se construye el mensaje JSON
				JsonObject textMessageFilter = new JsonObject(); //Es el objeto JSON que contendrá los datos del mensaje
				JsonObject credentials = new JsonObject(); // Es el objeto JSON que ontendrá las credenciales de Altiria
				
				// Se establecen las credenciales de autenticación
				credentials.addProperty("login", "noe.herrera@mobile-tic.com");
				credentials.addProperty("passwd", "M7Tc9pXbZh3d");
				

				
				// Se crea objeto JSON para consulta de credito
				JsonObject creditQuery = new JsonObject();
				creditQuery.add("credentials", credentials);
				
				
				// Se fija el tiempo máximo de espera para conectar con el servidor (5000)
				// Se fija el tiempo máximo de espera de la respuesta del servidor (60000)
				RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(60000).build();
				
				// Se inicia el objeto HTTP
				HttpClientBuilder builder = HttpClientBuilder.create();
				builder.setDefaultRequestConfig(config);
				CloseableHttpClient httpClient = builder.build();
				
				// Se fija la URL base de los recursos REST
				String baseUrl = "https://www.altiria.net:8443/apirest/ws";
				HttpPost request = new HttpPost(baseUrl + "/sendSms");
				
				
				// *********************            CONSULTA DE CREDITO                          *********************************
				
				//Creamos una istnacia de HTTP para la consulta de credito
				HttpPost creditRequest = new HttpPost(baseUrl + "/getCredit");
				
				creditRequest.addHeader("content-type", "application/json;charset=UTF-8");
				creditRequest.setEntity(new StringEntity(creditQuery.toString(), "UTF-8"));
				
				try {
					System.out.println("Enviando petición de consulta de crédito");
					CloseableHttpResponse creditResponse = httpClient.execute(creditRequest); // Enviamos una solicitud HTTP para consultar el credito disponible
					
					if (creditResponse.getStatusLine().getStatusCode() == 200) { // Validamos si el codigo de estado es 200
						
					    String creditResp = EntityUtils.toString(creditResponse.getEntity()); // Obtenemos la respuesta como una cadena de texto y la guardamos en la variable creditResp
					    
					    JsonObject creditJson = new JsonParser().parse(creditResp).getAsJsonObject();// Analizamos la cadena de texto de la respuesta como un JSON y lo convertimos a un objeto JSON
					    
					    if (creditJson.has("status") && creditJson.get("status").getAsString().equals("000")) { // verificamos si creditJson contiene una clave status y si su valor es "000"
					    	
					        String creditAmount = creditJson.get("credit").getAsString(); // Se obtenie el valor de la clave "credit" como una cadena de texto
					        double creditoDisponible = Double.parseDouble(creditAmount); // Se convierte creditAmount en una variable de tipo doble 
					        System.out.println("Crédito disponible: " + creditoDisponible);
					        
					        
					        int totalMensajes = 1; // Cantidad total de mensajes a enviar
							int costoPorMensaje = 1; // Costo en créditos de cada mensaje
							double creditosNecesarios = totalMensajes * costoPorMensaje;

							// Verificar si hay crédito suficiente para enviar todos los mensajes
							if (creditoDisponible < creditosNecesarios) {
								System.out.println("Error: No hay suficiente crédito disponible para enviar todos los mensajes.");
								return;

							}
							
					    } else {
					        System.out.println("ERROR: No se pudo obtener el crédito disponible de la respuesta");
					        return;
					    }
					} else {
					    System.out.println("ERROR: Código de error HTTP: " + creditResponse.getStatusLine().getStatusCode());
					    System.out.println("Compruebe que ha configurado correctamente la dirección/URL suministrada por Altiria");
					    return;
					}
				}catch(Exception e) {
					
				}
				//***********         FIN CONSULTA DE CREDITO        *******************
				
				
			
				 
		    	String numero = xNumeroCelular; // En la variable numero guardamos cada número del array para validar posteriormente con el if.
		        String regex = "^[0-9]{12}$";  // en la variable regex le especificamos el formato que debe de tener.
		        
		        // CONSULTAR EL MAXIMOREPORTE
		        
		        // Validamos si el formato de cada número cumple o no con el requisito de (12 numeros continuos sin letras).
		        if (!Pattern.matches(regex, numero)) {
		            System.out.println("Error: El número telefónico '" + numero + "' no cumple con el formato adecuado.");

		        }
		        
		    	// Se crea el Array de destinatarios 
				JsonArray destinations = new JsonArray(); // Este objeto de JSON ARRAY contendrá los números celular a enviar los sms 
				JsonPrimitive numeroDestino = new JsonPrimitive(numero);
			    destinations.add(numeroDestino);

				
				// Se crea un objeto JSON que contendrá el mensaje a enviar 
				JsonObject textMessage = new JsonObject();
				textMessage.addProperty("msg", xTextoSMS);
				
				// Se agregan los objetos credentials, destinatios y textMessage al objeto texMessageFilter para formar la estructura del mensaje JSON completo.
				textMessageFilter.add("credentials", credentials);
				textMessageFilter.add("destination", destinations);
				textMessageFilter.add("message", textMessage);
			
				// Se añade el JSON al cuerpo de la petición codificado en UTF-8
				request.setEntity(new StringEntity(textMessageFilter.toString(), "UTF-8"));
				
				// Se fija el tipo de contenido de la peticion POST
				request.addHeader("content-type", "application/json;charset=UTF-8");
				CloseableHttpResponse response = null;
				
				try {
					System.out.println("Enviando petición");
					response = httpClient.execute(request); // Se envía la petición
					String resp = EntityUtils.toString(response.getEntity()); // Se consigue la respuesta
					
					// Error en la respuesta del servidor
					if (response.getStatusLine().getStatusCode() != 200) {
						System.out.println("ERROR: Código de error HTTP: " + response.getStatusLine().getStatusCode());
						System.out.println("Compruebe que ha configurado correctamente la " + "direccion/url y el content-type");
								
						return;
					} else {
						// Se procesa la respuesta capturada en la cadena ’response’
						if (resp.startsWith("ERROR")) {
							System.out.println(resp);
							System.out.println("Código de error de Altiria. Compruebe las especificaciones");
						} else
							System.out.println(resp);
					}
				} catch (Exception e) {
					System.out.println("Excepción");
					e.printStackTrace();
					return;
				} finally {
					// En cualquier caso se cierra la conexión
					request.releaseConnection();
					if (response != null) {
						try {
							response.close();
						} catch (IOException ioe) {
							System.out.println("ERROR cerrando recursos");
						}
					}
				}
		
		
	}


}
