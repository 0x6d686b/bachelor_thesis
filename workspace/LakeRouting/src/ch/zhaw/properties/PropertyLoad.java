package ch.zhaw.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * This class loads the configuration-file, where all necessary entries are
 * available like pathnames etc.
 * 
 * @author Fevzi Yükseldi, Mathias Habluetzel
 * 
 */
public class PropertyLoad {

	private final static String DEFAULT_PATH = "config/configuration.properties";
	private String path;

	/**
	 * If another Property-File is needed, the path of the file can be passed
	 * here.
	 * 
	 * @param propertyPath
	 *            - The relative path of the File
	 */
	public PropertyLoad(String propertyPath) {
		path = propertyPath;
	}

	public PropertyLoad() {
		path = DEFAULT_PATH;
	}

	/**
	 * Returns the value of the entry from the config-file.
	 * 
	 * @param propertyName
	 *            - the identification of the entry
	 * @return the value of the entry.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String getValueOfProperty(String propertyName)
			throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(path));
		return prop.getProperty(propertyName);
	}

	/**
	 * Generates an URI with the value of the property and scheme, which are
	 * passed as parameters.
	 * 
	 * @param propertyName
	 *            - the identification of the entry
	 * @param scheme
	 *            - scheme name
	 * @return the URI
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public URI getURIOfProperty(String propertyName, String scheme)
			throws FileNotFoundException, IOException, URISyntaxException {
		return new URI(scheme, getValueOfProperty(propertyName), "");
	}

}
