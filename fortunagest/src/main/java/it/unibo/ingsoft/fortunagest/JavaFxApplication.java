package it.unibo.ingsoft.fortunagest;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class JavaFxApplication extends Application {

	private ConfigurableApplicationContext context;

	@Override
	public void init() throws Exception {

		ApplicationContextInitializer<GenericApplicationContext> initializer = new ApplicationContextInitializer<GenericApplicationContext>() {
			@Override
			public void initialize(GenericApplicationContext applicationContext) {

				applicationContext.registerBean(Application.class, () -> JavaFxApplication.this);
				// applicationContext.registerBean(Parameters.class);
				// applicationContext.registerBean(HostServices.class);

				// applicationContext.registerBean(Parameters.class, () ->
				// Parameters.getParameters );
				// applicationContext.registerBean(HostServices.class, );

			}
		};

		this.context = new SpringApplicationBuilder().sources(SpringLauncher.class).initializers(initializer)
				.run(getParameters().getRaw().toArray(new String[0]));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.context.publishEvent(new StageReadyEvent(primaryStage));

	}

	@Override
	public void stop() throws Exception {
		this.context.close();
		Platform.exit();
	}
}

class StageReadyEvent extends ApplicationEvent {
	public Stage getStage() {
		return Stage.class.cast(getSource());
	}

	public StageReadyEvent(Stage source) {
		super(source);
	}
}
