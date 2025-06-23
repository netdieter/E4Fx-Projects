/**
 * 
 */
package de.engelhardt.app;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.LoggerFactory;

//import ch.qos.logback.classic.LoggerContext;
////import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.classic.joran.JoranConfigurator;
//import ch.qos.logback.core.joran.spi.JoranException;

/**
 * 
 */
public class LifeCycleManager {

//	@PostContextCreate //: called after the application context has been created, but prior to the model having been loaded
//	private void configureLogbackExternal() throws JoranException, IOException {
//		Bundle bundle = FrameworkUtil.getBundle(LifeCycleManager.class);
//		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//	    JoranConfigurator jc = new JoranConfigurator();
//	    jc.setContext(context);
//	    context.reset();
//
//	    // Get the configuration location where the logback.xml is located
//	    Location configurationLocation = Platform.getConfigurationLocation();
//	    File logbackFile = new File(configurationLocation.getURL().getPath(), "logback.xml");
//
//	    if (logbackFile.exists()) {
//	        jc.doConfigure(logbackFile);
//	    } else {
//	        URL logbackConfigFileUrl = FileLocator.find(bundle, new Path("logback.xml"), null);
//	        jc.doConfigure(logbackConfigFileUrl.openStream());
//	    }
//	}
}
