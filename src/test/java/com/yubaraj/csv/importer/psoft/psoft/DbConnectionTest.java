
package com.yubaraj.csv.importer.psoft.psoft;

import org.junit.Assert;

import com.yubaraj.csv.importer.psoft.util.Initializer;

/**
 *
 * @author Yuba Raj Kalathoki
 */
public class DbConnectionTest {
    //@Test
    public void databaseConnectionTest(){
        Assert.assertNotNull(Initializer.getConnection());
    }
}
