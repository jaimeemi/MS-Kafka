package com.challengeTenpo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "server.port=0")
@ActiveProfiles("local-kafka")
class TabajoPracticoTenpoMicroservicioKafkaApplicationTests {

	@Test
	void contextLoads() {
		// Este test ahora simplemente verifica que el contexto de la aplicación
		// se carga correctamente con el perfil 'local-kafka',
		// incluyendo la base de datos H2 y el contenedor de Kafka.
	}
}