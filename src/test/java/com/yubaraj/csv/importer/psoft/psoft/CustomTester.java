package com.yubaraj.csv.importer.psoft.psoft;

import com.yubaraj.csv.importer.psoft.util.Initializer;

/**
 * Custom tester.
 * 
 * @author Yuba Raj Kalathoki
 */
public class CustomTester {
    public static void main(String[] args) {
	// List<CurrencyEnum> currencyEnums = new
	// ArrayList<CurrencyEnum>(Arrays.asList(CurrencyEnum.values()));
	// System.out.println(currencyEnums.size());
	// for(CurrencyEnum enum1: currencyEnums){
	// System.out.println(enum1);
	// }

	new Initializer().init();
	// System.out.println(JpaProcessor.getInstance().countByFromCurrencyCode("AWG",
	// BigInteger.valueOf(500000l)));

    }
}
