package sms_ApiRest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;



public class DBMailMarketing {
	
	public static void main(String[] args) {
        // Obtenemos la conexión a la base de datos
    	
    	
    	int xIdLocal = 0;
    	String xnumerosCelular =null;
    	int xidCampaigns = 0;
    	
    	
    	
        Connection connection = conexionSQLMailMarketing.getConexionMailMarketing();

        // Verificamos si la conexión es exitosa o no
        if (connection != null) {
            try {
                // Llamamos al método consultarTextoSMS
            	
                String xTextoSMS = consultarTextoSMS(connection, xIdLocal, xidCampaigns);
                
                String xFechaHora  = consultarFechayHora(connection, xIdLocal, xidCampaigns);
                
                
             
                
                int xIdMaximoReporte = obtenerMaximoReporte(connection);
                int xidCampaign = consultarIdCampaign(connection, xIdLocal, xidCampaigns);
                int xIdPlantilla = consultarIdPlantilla(connection, xIdLocal, xidCampaigns);
                
                int xIdDcto= consultaIdDcto(connection, xIdLocal);
                
                consultaCreditoLocal(connection, xIdLocal);
                consultaDebitoLocal(connection, xIdLocal);
                
         
                
                ingresaReporte(connection, xIdLocal, xIdMaximoReporte, xidCampaign, xIdPlantilla, xnumerosCelular, xTextoSMS, xIdDcto);
                incrementarDebito(connection, xIdLocal, xIdDcto);


                
                
                // Se cierra la conexión a la base de datos
                connection.close();
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    	//Obtenemos la conexion a la base de datos dbaquamovil
	     Connection connectionAquamovil =  conexionSQLaquamovil.getConexionAquamovil();
	    
	     
	        try {
	        	String[] xnumerosCelular1 = bdaquamovil.consultarTelefonoCelular(connectionAquamovil, xIdLocal);

	                 
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	            	connectionAquamovil.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
        
        
    }//Fin del main

                                             // EXTRAEMOS EL MENSAJE DE TEXTO 
    public static String consultarTextoSMS(Connection connection, int idLocal, int idCampaigns) throws SQLException {
        String textoSMS = "";

        // Ejecutamos una consulta parametrizada
        String queryTextoSMS = "SELECT * FROM tblMailCampaign WHERE idlocal = ? AND sistema = ? AND idCampaign = ?";
        PreparedStatement statement = connection.prepareStatement(queryTextoSMS);

        // Asignamos los valores de los parámetros en la consulta
        statement.setInt(1, idLocal); // idlocal
        statement.setString(2, "aquamovil"); // sistema
        statement.setInt(3, idCampaigns); // idCampaign

        // Ejecutamos la consulta y obtenemos el resultado
        ResultSet resultSet = statement.executeQuery();

        // Recorremos los resultados de la consulta
        while (resultSet.next()) {
            // Obtenemos los valores de cada columna en el registro actual
            int idLocales = resultSet.getInt("IDLOCAL");
            String NombreCampaign = resultSet.getString("NombreCampaign");
            String sistema = resultSet.getString("sistema");
            int idCampaign = resultSet.getInt("idCampaign");
            textoSMS = resultSet.getString("textoSMS");

            // Mostramos los valores por consola
            System.out.println("IDlocal: " + idLocales + " NombreCampaign: " + NombreCampaign + " sistema: " + sistema
                    + " idCampaign: " + idCampaign + " textoSMS: " + textoSMS);
        }

        // Cerramos los recursos utilizados
        resultSet.close();
        statement.close();
        

        // Retornamos el texto del SMS
        return textoSMS;
    }
    
    
    
    
                                                 // EXTRAEMOS LA FECHA Y HORA QUE ESTÁ PROGRAMADO EL ENVIO DEL SMS
    
    public static String consultarFechayHora(Connection connection, int idLocal, int idCampaigns)throws SQLException  {
    	String fechayHora = "";
    	
    	// Ejecutamos una consulta parametrizada
        String queryFechayHora = "SELECT * FROM tblMailCampaign WHERE idlocal = ? AND sistema = ? AND idCampaign = ?";
        PreparedStatement statement = connection.prepareStatement(queryFechayHora);
    	
        // Asignamos los valores de los parámetros en la consulta
        statement.setInt(1, idLocal); // idlocal
        statement.setString(2, "aquamovil"); // sistema
        statement.setInt(3, idCampaigns); // idCampaign
    	
        // Ejecutamos la consulta y obtenemos el resultado
        ResultSet resultSet = statement.executeQuery();
        
     // Recorremos los resultados de la consulta
        while (resultSet.next()) {
            // Obtenemos los valores de cada columna en el registro actual
            int idLocales = resultSet.getInt("IDLOCAL");
            String NombreCampaign = resultSet.getString("NombreCampaign");
            String sistema = resultSet.getString("sistema");
            int idCampaign = resultSet.getInt("idCampaign");
            fechayHora = resultSet.getString("fecha/hora");
            

            // Mostramos los valores por consola
            System.out.println("IDlocal: " + idLocales + " NombreCampaign: " + NombreCampaign + " sistema: " + sistema
                    + " idCampaign: " + idCampaign + " Fecha/Hora: " + fechayHora);
        }

        // Cerramos los recursos utilizados
        resultSet.close();
        statement.close();
        

        // Retornamos el texto del SMS
        
		return fechayHora;
    }
    
    
    
      								// EXTRAEMOS IDCAMPAÑA
    public static int consultarIdCampaign(Connection connection, int idLocal, int idCampaigns) throws SQLException {
        int idCampaign = 0;

        // Ejecutamos una consulta parametrizada
        String queryTextoSMS = "SELECT * FROM tblMailCampaign WHERE idlocal = ? AND sistema = ? AND idCampaign = ?";
        PreparedStatement statement = connection.prepareStatement(queryTextoSMS);

        // Asignamos los valores de los parámetros en la consulta
        statement.setInt(1, idLocal); // idlocal
        statement.setString(2, "aquamovil"); // sistema
        statement.setInt(3, idCampaigns); // idCampaign

        // Ejecutamos la consulta y obtenemos el resultado
        ResultSet resultSet = statement.executeQuery();

        // Recorremos los resultados de la consulta
        while (resultSet.next()) {
;
            idCampaign = resultSet.getInt("idCampaign");
            

            // Mostramos los valores por consola
            System.out.println("idCampaign: " + idCampaign);
        }

        // Cerramos los recursos utilizados
        resultSet.close();
        statement.close();
        

        // Retornamos el Id de la campaña
        return idCampaign;
    }
    
    
    
    								//EXTRAEMOS EL ID DE PLANTILLA
    public static int consultarIdPlantilla(Connection connection, int idLocal, int idCampaigns) throws SQLException {
        int idPlantilla = 0;

        // Ejecutamos una consulta parametrizada
        String queryTextoSMS = "SELECT * FROM tblMailCampaign WHERE idlocal = ? AND sistema = ? AND idCampaign = ?";
        PreparedStatement statement = connection.prepareStatement(queryTextoSMS);

        // Asignamos los valores de los parámetros en la consulta
        statement.setInt(1, idLocal); // idlocal
        statement.setString(2, "aquamovil"); // sistema
        statement.setInt(3, idCampaigns); // idCampaign

        // Ejecutamos la consulta y obtenemos el resultado
        ResultSet resultSet = statement.executeQuery();

        // Recorremos los resultados de la consulta
        while (resultSet.next()) {
 
        	idPlantilla = resultSet.getInt("idPlantilla");
            

            // Mostramos los valores por consola
            System.out.println("idPlantilla: " + idPlantilla);
        }

        // Cerramos los recursos utilizados
        resultSet.close();
        statement.close();
        

        // Retornamos el Id de la plantilla
        return idPlantilla;
    }
    
    
    
    
    
    
    
    
    
    
    								//OBTENEMOS EL REPORTE MÁXIMO DE IDREPORTE
	public static int obtenerMaximoReporte(Connection connection) throws SQLException{
		
		int maxIdReporte = 0;
		
		//Se define la consulta SQL para obtener el máximo valor de idReporte de la tabla tblMailMarketingReporte
		String queryObtenerMaxIdReporte = "SELECT MAX (idReporte) AS maxIdReporte FROM tblMailMarketingReporte";
		
		PreparedStatement statement = connection.prepareStatement(queryObtenerMaxIdReporte);
        ResultSet resultSet = statement.executeQuery();
        
        //se obtiene el valor de maxIdReporte del resultado utilizando el método getInt() del objeto ResultSet
        if (resultSet.next()) {
            maxIdReporte = resultSet.getInt("maxIdReporte");
        }
        
        //Cerramos para librar los recursos
        
        resultSet.close();
        statement.close();
        
        System.out.println("maxIdReporte: " + maxIdReporte);
        
		return maxIdReporte;
		
	}    
	
	
	
	
	                                      //CONSULTA CREDITO LOCAL
	public static int consultaCreditoLocal(Connection connection, int idLocal) throws SQLException {
		
		int creditoLocal = 0;
		
		
		String queryCreditoLocal = "SELECT * FROM tblMailCredito WHERE idlocal = ? AND sistema = ? AND idCampaign = ?";
		PreparedStatement statement = connection.prepareStatement(queryCreditoLocal);
		
		// Asignamos los valores de los parámetros en la consulta
        statement.setInt(1, idLocal); // idlocal
        statement.setString(2, "aquamovil"); // sistema
        statement.setInt(3, 18); // idCampaign

        // Ejecutamos la consulta y obtenemos el resultado
        ResultSet resultSet = statement.executeQuery();

        // Recorremos los resultados de la consulta
        while (resultSet.next()) {
 
        	creditoLocal = resultSet.getInt("credito");
        	
            

            // Mostramos los valores por consola
            System.out.println("creditoLocal: " + creditoLocal );
        }

        // Cerramos los recursos utilizados
        resultSet.close();
        statement.close();
        
		return creditoLocal;
	}
	
	
	
	
	                                   // CONSULTA DEBITO LOCAL 
	public static int consultaDebitoLocal(Connection connection, int idLocal) throws SQLException {
		
		int debitoLocal = 0;
		
		
		String queryCreditoLocal = "SELECT * FROM tblMailCredito WHERE idlocal = ? AND sistema = ? AND idCampaign = ?";
		PreparedStatement statement = connection.prepareStatement(queryCreditoLocal);
		
		// Asignamos los valores de los parámetros en la consulta
        statement.setInt(1, idLocal); // idlocal
        statement.setString(2, "aquamovil"); // sistema
        statement.setInt(3, 18); // idCampaign

        // Ejecutamos la consulta y obtenemos el resultado
        ResultSet resultSet = statement.executeQuery();

        // Recorremos los resultados de la consulta
        while (resultSet.next()) {
 
        	debitoLocal = resultSet.getInt("debito");
        	
            

            // Mostramos los valores por consola
            System.out.println("debitoLocal: " + debitoLocal );
        }

        // Cerramos los recursos utilizados
        resultSet.close();
        statement.close();
        
		return debitoLocal;
	}
	
	
	
	
	                                  // CONSULTA IdDcto LOCAL
	public static int consultaIdDcto(Connection connection, int idLocal) throws SQLException {
		
		int idDcto = 0;
		
		
		String queryCreditoLocal = "SELECT * FROM tblMailCredito WHERE idlocal = ? AND sistema = ? AND idCampaign = ?";
		PreparedStatement statement = connection.prepareStatement(queryCreditoLocal);
		
		// Asignamos los valores de los parámetros en la consulta
        statement.setInt(1, idLocal); // idlocal
        statement.setString(2, "aquamovil"); // sistema
        statement.setInt(3, 18); // idCampaign

        // Ejecutamos la consulta y obtenemos el resultado
        ResultSet resultSet = statement.executeQuery();

        // Recorremos los resultados de la consulta
        while (resultSet.next()) {
 
        	idDcto = resultSet.getInt("idDcto");
        	
            

            // Mostramos los valores por consola
            System.out.println("idDcto: " + idDcto );
        }

        // Cerramos los recursos utilizados
        resultSet.close();
        statement.close();
        
		return idDcto;
	}
	
	
	
		                         // GENERAMOS EL INSERT A LA TABLA tblMailCredito
		public static boolean incrementarDebito(Connection connection, int xIdLocal, int xIdDcto)throws SQLException {

			// Verificamos si la coenxión está cerrada, si es asi entonces se abre la conexión a la DB
			if (connection.isClosed()) {
				// Si la conexión está cerrada, la abrimos nuevamente
				connection = conexionSQLMailMarketing.getConexionMailMarketing();
			}

		    // Consultamos el valor actual de la columna "debito" para el local especificado
		    int debitoActual = 0;
		    
		    String selectStr = "SELECT debito FROM tblMailCredito WHERE IDLOCAL = ?";
		    
		    PreparedStatement selectStatement = connection.prepareStatement(selectStr);
		    selectStatement.setInt(1, xIdLocal);
		    ResultSet resultSet = selectStatement.executeQuery();
		    
		    if (resultSet.next()) {
		        debitoActual = resultSet.getInt("debito");
		    }
		    resultSet.close();
		    selectStatement.close();

		    // Incrementamos el valor de "debito" en 1
		    int nuevoDebito = debitoActual + 1;

		    // Actualizamos el valor de "debito" en la tabla "tblMailCredito" para el local especificado
		    String updateStr = "UPDATE tblMailCredito SET debito = ? WHERE IDLOCAL = ?";
		    PreparedStatement updateStatement = connection.prepareStatement(updateStr);
		    updateStatement.setInt(1, nuevoDebito);
		    updateStatement.setInt(2, xIdLocal);
		    updateStatement.executeUpdate();
		    updateStatement.close();

		    return true;
		}
		
		
		
	
	
	
    											// GENERAMOS EL INSERT A LA TABLA TBLMAILMARKETINGREPORTE
    public static boolean ingresaReporte(Connection connection, int xIdLocal, int xIdMaximoReporte, int xidCampaign, int xIdPlantilla, String xnumerosCelular, String xTextoSMS, int xIdDcto)throws SQLException  {
    	
    	  // Verificamos si la coenxión está cerrada, si es asi entonces se abre la conexión a la DB
    	if (connection.isClosed()) {
            // Si la conexión está cerrada, la abrimos nuevamente
            connection = conexionSQLMailMarketing.getConexionMailMarketing();
        }
    	
    
    	
    	String insertStr =  " INSERT INTO tblMailMarketingReporte "
    			+ "            (IDLOCAL               "// 1
    			+ "            ,sistema               "// 2  			
    			+ "            ,idCampaign            "// 3
    			+ "            ,idPlantilla           "// 4
    			+ "            ,idDcto                "// 5
    			+ "            ,idRequerimiento       "// 6
    			+ "            ,documentoTercero      "// 7
    			+ "            ,estado                "// 8
    			+ "            ,descripcion           "// 9
    			+ "            ,fechaHoraEvento       "// 10
    			+ "            ,exception             "// 11
    			+ "            ,email                 "// 12
    			+ "            ,celular)              "// 13
    			+ "  VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    	
    	                       
    	String xSistema = "aquamovil"; 	
//    	int xidDcto = 0;									
    	int xidRequerimiento = 1;						
    	int xestado = 1;
//    	String xdescripcion = "Envio SMS";
    	Timestamp xfechaHoraEvento = new Timestamp(System.currentTimeMillis()); 
    	String xexception = "";
    	String xemail = "";

    	

        
         PreparedStatement statement = connection.prepareStatement(insertStr);
        	
       
             statement.setInt(1, xIdLocal);
             statement.setString(2, xSistema);
             statement.setInt(3, xidCampaign);
             statement.setInt(4, xIdPlantilla);
             statement.setInt(5, xIdDcto);
             statement.setInt(6, xidRequerimiento);
             statement.setString(7, xnumerosCelular);
             statement.setInt(8, xestado);
             statement.setString(9, xTextoSMS);
             statement.setTimestamp(10, xfechaHoraEvento);
             statement.setString(11, xexception);
             statement.setString(12, xemail);
             statement.setString(13, xnumerosCelular);

             statement.executeUpdate();
       
            

            return true;
    
}

}
