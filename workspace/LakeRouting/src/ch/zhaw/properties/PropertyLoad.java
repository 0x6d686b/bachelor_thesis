package ch.zhaw.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class PropertyLoad {

	private final static String DEFAULT_PATH = "config/configuration.properties";
	private String path = DEFAULT_PATH;
	
	/**
	 * If someone needs another Property-File
	 * @param propertyPath - The relative path of the File
	 */
	public PropertyLoad(String propertyPath) {
		path = propertyPath;
	}
	
	public PropertyLoad() {
		
	}

	public String getValueOfProperty(String propertyName) throws FileNotFoundException, IOException{
		Properties prop = new Properties();
		prop.load(new FileInputStream(path));
		return prop.getProperty(propertyName);
	}
	
	public URI getURIOfProperty(String propertyName, String scheme) throws FileNotFoundException, IOException, URISyntaxException{
		return new URI(scheme, getValueOfProperty(propertyName), "");
	}
	
}
