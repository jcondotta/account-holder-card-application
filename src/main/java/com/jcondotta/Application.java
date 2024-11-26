package com.jcondotta;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import io.micronaut.context.env.Environment;
import io.micronaut.runtime.Micronaut;
import io.micronaut.serde.annotation.SerdeImport;

@SerdeImport(SNSEvent.class)
public class Application {

	public static void main(String[] args) {
		Micronaut.build(args)
				.mainClass(Application.class)
				.defaultEnvironments(Environment.DEVELOPMENT)
				.start();
	}
}
