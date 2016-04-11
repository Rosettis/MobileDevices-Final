package csci4100.uoit.ca.final_matthewrosettis;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Final_MatthewRosettis
 * Created by 100490515 on 12/8/2015.
 */

public class DownloadDataTask extends AsyncTask<String, String, ArrayList<Bike>> {
    private DataDownloadListener listener = null;
    private Exception exception = null;

    public DownloadDataTask(DataDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Bike> doInBackground(String... params) {
        // Get some stuff from the internet
        ArrayList<Bike> page = new ArrayList<>();
        try {
            // parse out the data
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            URL url = new URL(params[0]);
            Document document = docBuilder.parse(url.openStream());
            document.getDocumentElement().normalize();
            // look for <entry> tags and ge the data inside
            NodeList station = document.getElementsByTagName("station");
            for(int i = 0; i < station.getLength(); i++ ){
                if ((station.getLength() > 0) && (station.item(0).getNodeType() == Node.ELEMENT_NODE)) {
                    Element definitions = (Element)station.item(i);
                    NodeList idTags = definitions.getElementsByTagName("id");
                    String feedIdString = idTags.item(0).getTextContent();
                    int feedId = Integer.parseInt(feedIdString);
                    NodeList latTags = definitions.getElementsByTagName("lat");
                    String feedLatString = latTags.item(0).getTextContent();
                    double feedLat = Double.parseDouble(feedLatString);
                    NodeList longTags = definitions.getElementsByTagName("long");
                    String feedLongString = longTags.item(0).getTextContent();
                    double feedLong = Double.parseDouble(feedLongString);
                    NodeList nameTags = definitions.getElementsByTagName("terminalName");
                    String feedName = nameTags.item(0).getTextContent();
                    NodeList numBikeTags = definitions.getElementsByTagName("nbBikes");
                    String feedBikeNumString = numBikeTags.item(0).getTextContent();
                    int feedBikeNum = Integer.parseInt(feedBikeNumString);
                    NodeList addressTags = definitions.getElementsByTagName("name");
                    String feedAddress = addressTags.item(0).getTextContent();
                    page.add(new Bike(feedId,feedLat,feedLong,feedName,feedBikeNum,feedAddress));
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return page;
    }


    @Override
    protected void onPostExecute(ArrayList<Bike> b){
        if (exception != null) {
            exception.printStackTrace();
            return;
        }
        Log.d("InternetResourcesSample", "showing feed: " + b);
        listener.dataDownloaded(b);
    }
}
