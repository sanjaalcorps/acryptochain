package com.sanjaal.products.nepalflow.crawler.service.crypto.bscscan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @Author Kushal Paudyal
 * This crawler extracts the LIQ balance from the farm pool.
 * This uses Jsoup Library (org.jsoup) and can be found in maven repo.
 */
public class LiquidusPoolCrawler {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final int HTTP_TIMEOUT = 30000;

    private static HashMap balanceMap = new HashMap<String, Double>();
    private static HashMap poolMap = new HashMap<String, String>();
    public static final String ONE_MONTH_BNB_POOL = "0xb944b748A35B6dFFDd924bffD85910F968943a72";
    public static final String ONE_MONTH_POOL = "0xbebcd3ad501fc425a71cdc7593cedea324176e92";
    public static final String TWELVE_MONTHS_POOL = "0xc6AEd0e5B81383Fd8537f4f805492732BDf8efC0";
    public static final String THREE_MONTHS_POOL = "0x5ccd597728b1f088bfb749d9a9798ed0c6e2211c";
    public static final String BASE_URL = "https://bscscan.com/token/0xc7981767f644c7f8e483dabdc413e8a371b83079?a=";


    public static void main(String[] args) throws InterruptedException {
        poolMap.put(THREE_MONTHS_POOL, "3M Pool(LIQ)");
        poolMap.put(TWELVE_MONTHS_POOL, "12M Pool(LIQ)");
        poolMap.put(ONE_MONTH_BNB_POOL, "1M Pool(LIQ-BNB)");
        poolMap.put(ONE_MONTH_POOL, "1M Pool(LIQ)");

        LiquidusPoolCrawler crawler = new LiquidusPoolCrawler();
        crawler.crawlSingleWallet(BASE_URL, THREE_MONTHS_POOL);
        crawler.crawlSingleWallet(BASE_URL, TWELVE_MONTHS_POOL);
        crawler.crawlSingleWallet(BASE_URL, ONE_MONTH_BNB_POOL);
        crawler.crawlSingleWallet(BASE_URL, ONE_MONTH_POOL);

        System.out.println(balanceMap);

    }


    public synchronized void crawlSingleWallet(String baseUrl, String poolAddress) throws InterruptedException {
        Document document = crawlAsDocument(baseUrl + poolAddress);
        Element section = document.getElementById("ContentPlaceHolder1_divFilteredHolderBalance");

        String walletBalance = section.text();
        walletBalance = walletBalance.replaceAll("Balance ", "");
        walletBalance = walletBalance.replaceAll(" LIQ", "");
        balanceMap.put(poolMap.get(poolAddress), walletBalance);
        //System.out.println(walletBalance);
    }

    public Document crawlAsDocument(String url) {

        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent("Mozilla").timeout(HTTP_TIMEOUT).get();

        } catch (Exception e) {
            LOGGER.error("Could not crawl " + url + "\n" + e.getMessage());
        }

        return document;
    }

}
