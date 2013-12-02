package com.coreyang.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTest {

	public static void main(String[] args) throws IOException {
		Document doc = Jsoup.connect("http://www.fadhomme.cn/?product-4271.html").timeout(0).get();
		Elements items = doc.select("#listall_ll");
		Element tbodyItem = items.get(0);
		Elements trs = tbodyItem.select("tr");
		for (Element tr : trs) {
			boolean noFlag = false;
			Elements tdNo = tr.select("td:has(a)");
			if(tdNo.size()!=0){
				noFlag = true;
			}
			if (noFlag) {
				Elements sku = tr.select("[style]");
				System.out.println(sku.get(0).html());
			}
		}
	}
}
