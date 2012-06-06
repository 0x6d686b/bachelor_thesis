public class PropertyLoad {

	private final static String DEFAULT_PATH = "config/configuration.properties";
	private String path;

	public PropertyLoad(String propertyPath) {
		path = propertyPath;
	}

	public PropertyLoad() {
		path = DEFAULT_PATH;
	}

	public String getValueOfProperty(String propertyName)
			throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(path));
		return prop.getProperty(propertyName);
	}

	public URI getURIOfProperty(String propertyName, String scheme)
			throws FileNotFoundException, IOException, URISyntaxException {
		return new URI(scheme, getValueOfProperty(propertyName), "");
	}

}
