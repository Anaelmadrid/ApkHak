package com.example.application;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends Activity {

    private EditText textField_message;
    private Button button_send_post;
    private Button button_send_get;
    private TextView textView_response;
    private String url="http://10.66.243.72:5000/";//****Put your  URL here******
    private String POST="POST";
    private String GET="GET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		textField_message = findViewById(R.id.txtField_message);
		button_send_post = findViewById(R.id.button_send_post);
		button_send_get = findViewById(R.id.button_send_get);
		textView_response = findViewById(R.id.textView_response);

		/* Making a post request */
		button_send_post.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// Get the text in the text field. In this example you should type your name here
					String text = textField_message.getText().toString();
					if (text.isEmpty()) {
						textField_message.setError("This cannot be empty for post request");
					} else {
						/* If name text is not empty, then call the function to make the post request */
						sendRequest("POST", "getname", "name", text);
					}
				}
			});

		/* Making the get request */
		button_send_get.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					/* En nuestro archivo server.py implementamos un método get llamado "get_fact()". 
					* Allí especificamos su invocación de URL como '/getfact'. 
					* Aquí lo pasamos a la función sendRequest() */ 
					sendRequest("GET", "getfact", null, null);
				}
			});
}
		private void sendRequest(String type, String method, String paramname, String param) {
			/* Si la URL es para nuestra solicitud de obtención, no debería tener parámetros según nuestra implementación.
			* Pero nuestra solicitud de publicación debería tener el parámetro 'nombre'. */ 
			String fullURL = url + "/" + method + (param == null ? "" : "/" + param);
			Request request;

			OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS)
				.build();

			// Si es una solicitud de publicación, entonces tenemos que pasar los parámetros dentro del cuerpo de la solicitud 
			if ("POST".equals(type)) {
				RequestBody formBody = new FormBody.Builder()
					.add(paramname, param)
					.build();

				request = new Request.Builder()
					.url(fullURL)
					.post(formBody)
					.build();
			} else {
				/* Si es nuestra solicitud de obtención, no requiere parámetros, por lo tanto, solo se envía con la URL */ 
				request = new Request.Builder()
					.url(fullURL)
					.build();
			}

			/* Así es como se maneja la devolución de llamada */ 
			client.newCall(request).enqueue(new Callback() {
					@Override
					public void onFailure(Call call, IOException e) {
						e.printStackTrace();
					}

					@Override
					public void onResponse(Call call, Response response) throws IOException {
						// Read data on the worker thread
						final String responseData = response.body().string();

						// Ejecutar el código relacionado con la vista nuevamente en el hilo principal.
						// Aquí mostramos el mensaje de respuesta en nuestra vista de texto 
						runOnUiThread(new Runnable() {
								@Override
								public void run() {
									textView_response.setText(responseData);
								}
							});
					}
				});
		}
		}
