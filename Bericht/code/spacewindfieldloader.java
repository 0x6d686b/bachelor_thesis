    /**
     * Reads the FileReader input
     */
    private void read(FileReader fis) {
        BufferedReader br = new BufferedReader(fis);
        String s;
            while ((s = br.readLine()) != null) {
                s = s.replaceAll("\\s+$", "");
                Matcher header = HEADER_START_PATTERN.matcher(s);
                if (header.find()) {
                    if (field != null)
                        windfieldArray.add(Windfield.getInstance().setField(getMetadata(),
                                           convertToList()));
                    field = new ArrayList<List<Object>>();
                    field.add(processHeader(s));
                    continue;
                }
                if (!s.isEmpty()) {
                    field.add(processLine(s));
                }
            }
            br.close();
            windfieldArray.add(Windfield.getInstance().setField(getMetadata(),
                               convertToList()));
        }
    }

    /**
     * Parses the line as a line full of wind vector pairs.
     */
    private List<Object> processLine(String input) {
        int i = 0;
        List<Object> arr = new ArrayList<Object>();

        Scanner scanner = new Scanner(input).useLocale(Locale.ENGLISH);
        scanner.useDelimiter(" ");
            while(scanner.hasNext()) {

                // Do we have the row identifier (the latitude)?
                if (i == 0) {
                    arr.add(scanner.nextDouble());
                    i++;
                    continue;
                }

                // We should now only have some wind vectors left
                WindVector v;
                    v = new WindVector(scanner.nextDouble(),
                                       scanner.nextDouble());
                arr.add(v);
                i++;
            }
            scanner.close();
        }
        return arr;
    }

    /**
     * Parses the header of a wind field block
     */
    private List<Object> processHeader(String input) {
        int i = 0;
        List<Object> arr = new ArrayList<Object>();

        Scanner scanner = new Scanner(input).useLocale(Locale.ENGLISH);
        scanner.useDelimiter(" ");
            while(scanner.hasNext()) {
                if (i == 0) {
                    arr.add(scanner.next(HEADER_START_PATTERN));
                    i++;
                    continue;
                }
                arr.add(scanner.nextDouble());
                i++;
            }
        }
        return arr;    
    }
