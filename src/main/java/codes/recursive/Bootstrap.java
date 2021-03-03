package codes.recursive;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class Bootstrap implements ApplicationEventListener<ApplicationStartupEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    public Bootstrap( ) { }
    @Override
    public void onApplicationEvent(ApplicationStartupEvent event) {
        LOG.info("Application started!");
        LOG.info("Bootstrap complete.");
    }
}