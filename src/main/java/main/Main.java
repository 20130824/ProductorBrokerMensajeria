package main;

import jms.Productor;
import org.apache.activemq.broker.BrokerService;
import org.json.JSONObject;

import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Ejemplo para probar el uso de JMS, utilizando servidor de mensajeria ActiveMQ, ver en
 * http://activemq.apache.org/
 * Created by vacax on 03/10/15.
 */
public class Main {

    public static void main(String[] args) throws IOException, JMSException  {
        System.out.println("Prueba de Mensajeria Asincrona");
        String cola = "pruebajms.cola";
        Productor productor = new Productor();

        Runnable helloRunnable = new Runnable() {
            public void run() {
                try {

                    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DDTHH:MM:SS");
                    Date fecha = new Date();
                    double temp = 25.1 + Math.random() * (34.9 - 25.1);
                    double hum = 70.1 + Math.random() * (89.9 - 70.1);

                    String jsonString = new JSONObject()
                            .put("fechaGeneracion", dateFormat.format(fecha))
                            .put("idDispositivo", 1)
                            .put("temperatura", round(temp, 2))
                            .put("humedad", round(hum, 2))
                            .toString();
                    productor.enviarMensaje(cola, jsonString);

                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);





    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }



}