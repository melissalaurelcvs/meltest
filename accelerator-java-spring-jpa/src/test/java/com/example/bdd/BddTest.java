package com.example.bdd;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasspathResource("bdd")
@ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_ENABLED_PROPERTY_NAME, value = "false")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, json:target/cucumber-reports/Cucumber.json, junit:target/cucumber-reports/Cucumber.xml, html:target/cucumber-reports.html")
@ConfigurationParameter(key = "cucumber.monochrome", value = "true")
@SuppressWarnings("java:S2187")
public class BddTest {
}
