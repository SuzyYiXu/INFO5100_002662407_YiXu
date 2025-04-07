import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;

public class BookShelfParser {
    public static void main(String[] args) {
        // Sample XML data
        String xmlData = """
                <BookShelf>
                    <Book>
                        <title>Java Programming</title>
                        <publishedYear>2020</publishedYear>
                        <numberOfPages>450</numberOfPages>
                        <authors>
                            <author>John Doe</author>
                            <author>Jane Smith</author>
                        </authors>
                    </Book>
                    <Book>
                        <title>Python Basics</title>
                        <publishedYear>2019</publishedYear>
                        <numberOfPages>320</numberOfPages>
                        <authors>
                            <author>Alice Johnson</author>
                        </authors>
                    </Book>
                    <Book>
                        <title>Data Structures</title>
                        <publishedYear>2021</publishedYear>
                        <numberOfPages>600</numberOfPages>
                        <authors>
                            <author>Robert Brown</author>
                            <author>Emily Davis</author>
                            <author>Michael Wilson</author>
                        </authors>
                    </Book>
                </BookShelf>
                """;

        // Sample JSON data
        String jsonData = """
                {
                  "books": [
                    {
                      "title": "Java Programming",
                      "publishedYear": 2020,
                      "numberOfPages": 450,
                      "authors": ["John Doe", "Jane Smith"]
                    },
                    {
                      "title": "Python Basics",
                      "publishedYear": 2019,
                      "numberOfPages": 320,
                      "authors": ["Alice Johnson"]
                    },
                    {
                      "title": "Data Structures",
                      "publishedYear": 2021,
                      "numberOfPages": 600,
                      "authors": ["Robert Brown", "Emily Davis", "Michael Wilson"]
                    }
                  ]
                }
                """;

        System.out.println("=== ORIGINAL DATA ===");
        System.out.println("\nXML Format:");
        parseAndPrintXML(xmlData);

        System.out.println("\nJSON Format:");
        parseAndPrintJSON(jsonData);

        System.out.println("\n=== ADDING NEW BOOKS ===");

        // Add to XML and show
        String updatedXml = addBookToXML(xmlData);
        System.out.println("\nUPDATED XML (4 books total):");
        parseAndPrintXML(updatedXml);

        // Add to JSON and show
        String updatedJson = addBookToJSON(jsonData);
        System.out.println("\nUPDATED JSON (4 books total):");
        parseAndPrintJSON(updatedJson);
    }

    public static void parseAndPrintXML(String xmlData) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

            NodeList bookList = doc.getElementsByTagName("Book");
            System.out.println("Total books found: " + bookList.getLength());

            for (int i = 0; i < bookList.getLength(); i++) {
                Node bookNode = bookList.item(i);
                if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element bookElement = (Element) bookNode;

                    System.out.println("\nBook #" + (i+1));
                    System.out.println("Title: " + getElementValue(bookElement, "title"));
                    System.out.println("Published Year: " + getElementValue(bookElement, "publishedYear"));
                    System.out.println("Number of Pages: " + getElementValue(bookElement, "numberOfPages"));

                    NodeList authors = bookElement.getElementsByTagName("author");
                    System.out.print("Authors: ");
                    for (int j = 0; j < authors.getLength(); j++) {
                        if (j > 0) System.out.print(", ");
                        System.out.print(authors.item(j).getTextContent());
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parseAndPrintJSON(String jsonData) {
        try {
            // Extract the books array
            int arrayStart = jsonData.indexOf('[');
            int arrayEnd = jsonData.lastIndexOf(']');
            if (arrayStart == -1 || arrayEnd == -1) {
                System.out.println("No books array found");
                return;
            }

            String booksArray = jsonData.substring(arrayStart + 1, arrayEnd);

            // Split into individual book entries
            List<String> bookEntries = new ArrayList<>();
            int braceCount = 0;
            int start = -1;

            for (int i = 0; i < booksArray.length(); i++) {
                char c = booksArray.charAt(i);
                if (c == '{') {
                    if (braceCount == 0) start = i;
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0 && start != -1) {
                        bookEntries.add(booksArray.substring(start, i + 1));
                    }
                }
            }

            System.out.println("Total books found: " + bookEntries.size());

            // Parse and print each book
            for (int i = 0; i < bookEntries.size(); i++) {
                String bookJson = bookEntries.get(i);

                System.out.println("\nBook #" + (i + 1));
                System.out.println("Title: " + extractJsonField(bookJson, "title"));
                System.out.println("Published Year: " + extractJsonField(bookJson, "publishedYear"));
                System.out.println("Number of Pages: " + extractJsonField(bookJson, "numberOfPages"));
                System.out.println("Authors: " + extractAuthors(bookJson));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    private static String extractJsonField(String json, String field) {
        String pattern = "\"" + field + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return "";

        start += pattern.length();
        int end = findJsonValueEnd(json, start);

        String value = json.substring(start, end).trim();
        // Remove quotes if present
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static int findJsonValueEnd(String json, int start) {
        boolean inQuotes = false;
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inQuotes = !inQuotes;
            } else if (!inQuotes && (c == ',' || c == '}' || c == ']')) {
                return i;
            }
        }
        return json.length();
    }

    private static String extractAuthors(String json) {
        int start = json.indexOf("\"authors\":");
        if (start == -1) return "";

        start = json.indexOf('[', start);
        if (start == -1) return "";
        start++;

        int end = json.indexOf(']', start);
        if (end == -1) return "";

        String authorsContent = json.substring(start, end).trim();
        if (authorsContent.isEmpty()) return "";

        // Split authors and clean up
        List<String> authors = new ArrayList<>();
        int quoteStart = -1;
        boolean inQuote = false;

        for (int i = 0; i < authorsContent.length(); i++) {
            char c = authorsContent.charAt(i);
            if (c == '"' && (i == 0 || authorsContent.charAt(i - 1) != '\\')) {
                if (!inQuote) {
                    quoteStart = i + 1;
                    inQuote = true;
                } else {
                    authors.add(authorsContent.substring(quoteStart, i));
                    inQuote = false;
                }
            }
        }

        return String.join(", ", authors);
    }

    public static String addBookToXML(String xmlData) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

            Element root = doc.getDocumentElement();

            // Create new book element
            Element newBook = doc.createElement("Book");

            // Add title
            Element title = doc.createElement("title");
            title.appendChild(doc.createTextNode("Advanced Algorithms"));
            newBook.appendChild(title);

            // Add published year
            Element year = doc.createElement("publishedYear");
            year.appendChild(doc.createTextNode("2022"));
            newBook.appendChild(year);

            // Add pages
            Element pages = doc.createElement("numberOfPages");
            pages.appendChild(doc.createTextNode("780"));
            newBook.appendChild(pages);

            // Add authors
            Element authors = doc.createElement("authors");
            Element author1 = doc.createElement("author");
            author1.appendChild(doc.createTextNode("Sarah Miller"));
            authors.appendChild(author1);
            Element author2 = doc.createElement("author");
            author2.appendChild(doc.createTextNode("David Taylor"));
            authors.appendChild(author2);
            newBook.appendChild(authors);

            // Add to document
            root.appendChild(newBook);

            // Convert back to string
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return xmlData;
        }
    }

    public static String addBookToJSON(String jsonData) {
        // Find where to insert the new book (just before the closing ])
        int insertPos = jsonData.lastIndexOf(']');
        if (insertPos == -1) return jsonData;

        // Create the new book JSON
        String newBook = """
                ,
                {
                  "title": "Advanced Algorithms",
                  "publishedYear": 2022,
                  "numberOfPages": 780,
                  "authors": ["Sarah Miller", "David Taylor"]
                }""";

        // Insert the new book
        String updatedJson = jsonData.substring(0, insertPos) + newBook + jsonData.substring(insertPos);

        return updatedJson;
    }
}