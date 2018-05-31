package net.epixdude.memebot.crypto;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CryptoGraph
{
    private static final String URL_FIRST_PART = "https://min-api.cryptocompare.com/data/histohour?fsym=";
    private static final String URL_SECOND_PART = "&tsym=USD&limit=23";
    private static final String USER_AGENT = "Mozilla/5.0";
    public static void main( String[] args )
    {
        CryptoGraph.graph( "ETH", "chart.png" );
    }

    private static JSONArray getData( String symbol ) throws IOException
    {
        String urlToFetch = URL_FIRST_PART;
        urlToFetch += symbol;
        urlToFetch += URL_SECOND_PART;
        final URL statsURL = new URL( urlToFetch );
        final HttpURLConnection statsCon = (HttpURLConnection) statsURL.openConnection();
        statsCon.setRequestMethod( "GET" );
        statsCon.setRequestProperty( "User-Agent", USER_AGENT );
        // get the data
        final BufferedReader in = new BufferedReader( new InputStreamReader( statsCon.getInputStream() ) );
        final StringBuffer response = new StringBuffer();

        // read the data into a buffer
        String inputLine;
        while ( (inputLine = in.readLine()) != null ) {
            response.append( inputLine );
        }
        in.close();
        // parse the json
        final JSONObject j = new JSONObject( response.toString() );
        return j.getJSONArray( "Data" );

    }
    private static DefaultCategoryDataset initData( String symbol )
    {
        DefaultCategoryDataset data = new DefaultCategoryDataset(  );
        try{
            JSONArray jdata = getData( symbol );
            for(int i = 0; i < 24; i++)
            {
                data.addValue( jdata.getJSONObject( i ).getDouble( "close" ), symbol, Integer.toString( 24-i )  );
            }
        } catch( IOException ioe )
        {
            ioe.printStackTrace();
        }
        return data;
    }
    public static String graph( String symbol, String file )
    {
        DefaultCategoryDataset data = initData( symbol );
        JFreeChart chart = ChartFactory.createLineChart(
                symbol + " price",
                "hours",
                "price",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        NumberAxis y = new NumberAxis( "price" );
        y.setAutoRangeIncludesZero( false );
        chart.getCategoryPlot().setRangeAxis( y );
        try
        {
            File f = new File(file);
            ChartUtilities.saveChartAsPNG( f, chart, 600, 400 );
        }catch ( IOException ioe )
        {
            ioe.printStackTrace();
        }
        return file;
    }
}
