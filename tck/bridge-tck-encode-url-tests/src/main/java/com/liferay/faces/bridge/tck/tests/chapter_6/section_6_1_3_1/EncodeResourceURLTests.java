/**
 * Copyright (c) 2000-2022 Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liferay.faces.bridge.tck.tests.chapter_6.section_6_1_3_1;

import java.io.UnsupportedEncodingException;

import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.WindowState;
import javax.portlet.faces.Bridge;
import javax.portlet.faces.BridgeUtil;

import com.liferay.faces.bridge.tck.annotation.BridgeTest;
import com.liferay.faces.bridge.tck.beans.TestBean;
import com.liferay.faces.bridge.tck.common.Constants;
import com.liferay.faces.bridge.tck.common.util.HTTPUtils;


/**
 * @author  Neil Griffin
 */
public class EncodeResourceURLTests {

	// Test is SingleRequest -- Render only
	// Test #6.32
	@BridgeTest(test = "encodeResourceURLBackLinkTest")
	public String encodeResourceURLBackLinkTest(TestBean testBean) {
		testBean.setTestComplete(true);

		final String URL_BACKLINK_TEST_STRING = "/resources/myImage.jpg?javax.portlet.faces.BackLink=myBackLinkParam";
		final String URL_BACKLINK_VERIFY_STRING = "/resources/myImage.jpg?myBackLinkParam=";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		ViewHandler viewHandler = facesContext.getApplication().getViewHandler();

		// compute what the backLink should be
		String actionURL = externalContext.encodeActionURL(viewHandler.getActionURL(facesContext,
					facesContext.getViewRoot().getViewId()));
		String testString = viewHandler.getResourceURL(facesContext, URL_BACKLINK_TEST_STRING);
		String verifyString = null;

		try {
			verifyString = viewHandler.getResourceURL(facesContext, URL_BACKLINK_VERIFY_STRING) +
				HTTPUtils.encode(actionURL, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			testBean.setTestResult(false, "Failed because couldn't UTF-8 encode backLink parameter.");

			return Constants.TEST_FAILED;
		}

		// According to bridge rules since string passed in isn't xml strict encoded the result won't be as well
		// So ensure compares match by stripping from the one generated by the portlet container (if it exists)
		if (externalContext.encodeResourceURL(testString).equals(
					((PortletResponse) externalContext.getResponse()).encodeURL(verifyString).replace("&amp;", "&"))) {
			testBean.setTestResult(true, "encodeResourceURL correctly encoded an URL with a backLink.");

			return Constants.TEST_SUCCESS;
		}
		else {
			testBean.setTestResult(false,
				"encodeResourceURL didn't correctly encoded an URL with a backLink.  Expected: " +
				((PortletResponse) externalContext.getResponse()).encodeURL(verifyString) +
				" but encodeResourceURL returned: " + externalContext.encodeResourceURL(testString));

			return Constants.TEST_FAILED;
		}
	}

	// Test is SingleRequest -- Render only
	// Test #6.27
	@BridgeTest(test = "encodeResourceURLForeignExternalURLBackLinkTest")
	public String encodeResourceURLForeignExternalURLBackLinkTest(TestBean testBean) {
		testBean.setTestComplete(true);

		final String FOREIGNEXTERNALURL_BACKLINK_TEST_STRING =
			"http://www.apache.org?javax.portlet.faces.BackLink=myBackLinkParam";
		final String FOREIGNEXTERNALURL_BACKLINK_VERIFY_STRING = "http://www.apache.org?myBackLinkParam=";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		// compute what the backLink should be
		String actionURL = externalContext.encodeActionURL(facesContext.getApplication().getViewHandler().getActionURL(
					facesContext, facesContext.getViewRoot().getViewId()));
		String verifyString = null;

		try {
			verifyString = FOREIGNEXTERNALURL_BACKLINK_VERIFY_STRING + HTTPUtils.encode(actionURL, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			testBean.setTestResult(false, "Failed because couldn't UTF-8 encode backLink parameter.");

			return Constants.TEST_FAILED;
		}

		// According to bridge rules since string passed in isn't xml strict encoded the result won't be as well
		// So ensure compares match by stripping from the one generated by the portlet container (if it exists)
		if (externalContext.encodeResourceURL(FOREIGNEXTERNALURL_BACKLINK_TEST_STRING).equals(
					((PortletResponse) externalContext.getResponse()).encodeURL(verifyString).replace("&amp;", "&"))) {
			testBean.setTestResult(true, "encodeResourceURL correctly encoded a foreign external URL with a backLink.");

			return Constants.TEST_SUCCESS;
		}
		else {
			testBean.setTestResult(false,
				"encodeResourceURL didn't correctly encoded a foreign external URL with a backLink.  Expected: " +
				((PortletResponse) externalContext.getResponse()).encodeURL(verifyString) +
				" but encodeResourceURL returned: " +
				externalContext.encodeResourceURL(FOREIGNEXTERNALURL_BACKLINK_TEST_STRING));

			return Constants.TEST_FAILED;
		}
	}

	// Test is SingleRequest -- Render only
	// Test #6.28
	@BridgeTest(test = "encodeResourceURLForeignExternalURLTest")
	public String encodeResourceURLForeignExternalURLTest(TestBean testBean) {
		testBean.setTestComplete(true);

		final String FOREIGNEXTERNALURL_TEST_STRING = "http://www.apache.org";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		if (externalContext.encodeResourceURL(FOREIGNEXTERNALURL_TEST_STRING).equals(
					((PortletResponse) externalContext.getResponse()).encodeURL(FOREIGNEXTERNALURL_TEST_STRING).replace(
						"&amp;", "&"))) {
			testBean.setTestResult(true, "encodeResourceURL correctly encoded a foreign external URL.");

			return Constants.TEST_SUCCESS;
		}
		else {
			testBean.setTestResult(false,
				"encodeResourceURL didn't correctly encoded a foreign external URL.  Expected: " +
				((PortletResponse) externalContext.getResponse()).encodeURL(FOREIGNEXTERNALURL_TEST_STRING) +
				" and encodeResourceURL returned: " +
				externalContext.encodeResourceURL(FOREIGNEXTERNALURL_TEST_STRING));

			return Constants.TEST_FAILED;
		}
	}

	// Test is SingleRequest -- Render only
	// Test #6.26
	/**
	 * encodeResourceURLTests
	 */
	@BridgeTest(test = "encodeResourceURLOpaqueTest")
	public String encodeResourceURLOpaqueTest(TestBean testBean) {
		testBean.setTestComplete(true);

		final String OPAQUE_TEST_STRING = "mailto:jsr-301-comments@jcp.org";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		if (externalContext.encodeResourceURL(OPAQUE_TEST_STRING).equals(OPAQUE_TEST_STRING)) {
			testBean.setTestResult(true,
				"encodeResourceURL correctly returned an unchanged string when the input was an opaque URL.");

			return Constants.TEST_SUCCESS;
		}
		else {
			testBean.setTestResult(false,
				"encodeResourceURL didn't return an unchanged string when the input was an opaque URL.  Test parameter: " +
				OPAQUE_TEST_STRING + " and encodeResourceURL returned: " +
				externalContext.encodeResourceURL(OPAQUE_TEST_STRING));

			return Constants.TEST_FAILED;
		}
	}

	// Test is SingleRequest -- Render only
	// Test #6.30
	@BridgeTest(test = "encodeResourceURLRelativeURLBackLinkTest")
	public String encodeResourceURLRelativeURLBackLinkTest(TestBean testBean) {
		testBean.setTestComplete(true);

		final String RELATIVEURL_BACKLINK_TEST_STRING =
			"../resources/myImage.jpg?javax.portlet.faces.BackLink=myBackLinkParam";
		final String RELATIVEURL_BACKLINK_VERIFY_STRING = "/resources/myImage.jpg?myBackLinkParam=";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		// compute what the backLink should be
		String actionURL = externalContext.encodeActionURL(facesContext.getApplication().getViewHandler().getActionURL(
					facesContext, facesContext.getViewRoot().getViewId()));
		String verifyString = null;

		try {
			verifyString = externalContext.getRequestContextPath() + RELATIVEURL_BACKLINK_VERIFY_STRING +
				HTTPUtils.encode(actionURL, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			testBean.setTestResult(false, "Failed because couldn't UTF-8 encode backLink parameter.");

			return Constants.TEST_FAILED;
		}

		// According to bridge rules since string passed in isn't xml strict encoded the result won't be as well
		// So ensure compares match by stripping from the one generated by the portlet container (if it exists)
		if (externalContext.encodeResourceURL(RELATIVEURL_BACKLINK_TEST_STRING).equals(
					((PortletResponse) externalContext.getResponse()).encodeURL(verifyString).replace("&amp;", "&"))) {
			testBean.setTestResult(true, "encodeResourceURL correctly encoded a relative URL with a backLink.");

			return Constants.TEST_SUCCESS;
		}
		else {
			testBean.setTestResult(false,
				"encodeResourceURL didn't correctly encoded a relative URL with a backLink.  Expected: " +
				verifyString + " but encodeResourceURL returned: " +
				externalContext.encodeResourceURL(RELATIVEURL_BACKLINK_TEST_STRING));

			return Constants.TEST_FAILED;
		}
	}

	// Test is SingleRequest -- Render only
	// Test #6.29
	@BridgeTest(test = "encodeResourceURLRelativeURLTest")
	public String encodeResourceURLRelativeURLTest(TestBean testBean) {
		testBean.setTestComplete(true);

		final String RELATIVEURL_TEST_STRING = "../resources/myImage.jpg";
		final String RELATIVEURL_VERIFY_STRING = "/resources/myImage.jpg";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		if (externalContext.encodeResourceURL(RELATIVEURL_TEST_STRING).equals(
					((PortletResponse) externalContext.getResponse()).encodeURL(
						externalContext.getRequestContextPath() + RELATIVEURL_VERIFY_STRING).replace("&amp;", "&"))) {
			testBean.setTestResult(true,
				"encodeResourceURL correctly encoded a resource referenced by a relative path.");

			return Constants.TEST_SUCCESS;
		}
		else {
			testBean.setTestResult(false,
				"encodeResourceURL incorrectly encoded a resource referenced by a relative path.  Expected: " +
				((PortletResponse) externalContext.getResponse()).encodeURL(
					externalContext.getRequestContextPath() + RELATIVEURL_VERIFY_STRING) +
				" and encodeResourceURL returned: " + externalContext.encodeResourceURL(RELATIVEURL_TEST_STRING));

			return Constants.TEST_FAILED;
		}
	}

	// Test is SingleRequest -- Render only
	// Test #6.31
	@BridgeTest(test = "encodeResourceURLTest")
	public String encodeResourceURLTest(TestBean testBean) {
		testBean.setTestComplete(true);

		final String URL_TEST_STRING = "/myportal/resources/myImage.jpg";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		String testURL = externalContext.encodeResourceURL(URL_TEST_STRING);
		String verifyURL = ((PortletResponse) externalContext.getResponse()).encodeURL(URL_TEST_STRING).replace("&amp;",
				"&");

		if (testURL.equals(verifyURL)) {
			testBean.setTestResult(true,
				"encodeResourceURL correctly encoded the resource as an external (App) resource.");

			return Constants.TEST_SUCCESS;
		}
		else {
			testBean.setTestResult(false,
				"encodeResourceURL incorrectly encoded a resource as if it were a reference to a resource within this application.  Generated: " +
				testURL + " but expected: " + verifyURL);

			return Constants.TEST_FAILED;
		}
	}

	// Test is SingleRequest -- Render only
	// Test #6.33
	@BridgeTest(test = "encodeResourceURLViewLinkTest")
	public String encodeResourceURLViewLinkTest(TestBean testBean) {
		testBean.setTestComplete(true);

		// assume web.xml does Faces suffix mapping of .jsf to .jsp
		final String URL_VIEWLINK_TEST_STRING =
			"/tests/viewLink.jsf?javax.portlet.faces.ViewLink=true&param1=testValue";
		final String URL_VIEWLINK_VERIFY_STRING = "/tests/viewLink.jsf?param1=testValue";

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		ViewHandler viewHandler = facesContext.getApplication().getViewHandler();

		String testString = viewHandler.getResourceURL(facesContext, URL_VIEWLINK_TEST_STRING);
		String verifyString = viewHandler.getResourceURL(facesContext, URL_VIEWLINK_VERIFY_STRING);

		if (externalContext.encodeResourceURL(testString).equals(externalContext.encodeActionURL(verifyString))) {
			testBean.setTestResult(true, "encodeResourceURL correctly encoded a viewLink.");

			return Constants.TEST_SUCCESS;
		}
		else {
			testBean.setTestResult(false,
				"encodeResourceURL incorrectly encoded a viewLink.  Expected: " +
				externalContext.encodeActionURL(verifyString) + " but encodeResourceURL with the viewLink returned: " +
				externalContext.encodeResourceURL(testString));

			return Constants.TEST_FAILED;
		}
	}

	// Test is SingleRequest -- Render only
	// Test #6.34
	@BridgeTest(test = "encodeResourceURLViewLinkWithBackLinkTest")
	public String encodeResourceURLViewLinkWithBackLinkTest(TestBean testBean) {
		testBean.setTestComplete(true);

		final String URL_VIEWLINK_BACKLINK_TEST_STRING =
			"/tests/viewLink.jsf?javax.portlet.faces.ViewLink=true&param1=testValue&javax.portlet.faces.BackLink=myBackLinkParam";
		final String URL_VIEWLINK_BACKLINK_VERIFY_STRING = "/tests/viewLink.jsf?param1=testValue&myBackLinkParam=";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		ViewHandler viewHandler = facesContext.getApplication().getViewHandler();

		// compute what the backLink should be
		String actionURL = externalContext.encodeActionURL(facesContext.getApplication().getViewHandler().getActionURL(
					facesContext, facesContext.getViewRoot().getViewId()));
		String testString = viewHandler.getResourceURL(facesContext, URL_VIEWLINK_BACKLINK_TEST_STRING);
		String verifyString = null;

		try {
			verifyString = viewHandler.getResourceURL(facesContext,
					URL_VIEWLINK_BACKLINK_VERIFY_STRING + HTTPUtils.encode(actionURL, "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			testBean.setTestResult(false, "Failed because couldn't UTF-8 encode backLink parameter.");

			return Constants.TEST_FAILED;
		}

		// Note:  as we are adding a URL as a parameter make sure its properly URLEncoded -- this causes it to be
		// doubly encoded
		if (externalContext.encodeResourceURL(testString).equals(externalContext.encodeActionURL(verifyString))) {
			testBean.setTestResult(true, "encodeResourceURL correctly encoded a viewLink with a BackLink.");

			return Constants.TEST_SUCCESS;
		}
		else {
			testBean.setTestResult(false,
				"encodeResourceURL incorrectly encoded a viewLink.  Expected: " +
				externalContext.encodeActionURL(verifyString) + " but encodeResourceURL with the viewLink returned: " +
				externalContext.encodeResourceURL(testString));

			return Constants.TEST_FAILED;
		}
	}

	// Test is MultiRequest -- Render/Action
	// Test #6.136
	@BridgeTest(test = "encodeResourceURLWithModeTest")
	public String encodeResourceURLWithModeTest(TestBean testBean) {

		// This tests that we can encode a new mode in an actionURL
		// done by navigation rule.
		FacesContext facesContext = FacesContext.getCurrentInstance();

		if (BridgeUtil.getPortletRequestPhase(facesContext) == Bridge.PortletPhase.RESOURCE_PHASE) {
			PortletRequest portletRequest = (PortletRequest) facesContext.getExternalContext().getRequest();

			// Parameter/Mode encoded in the faces-config.xml target
			PortletMode portletMode = portletRequest.getPortletMode();

			// Check that the parameter came along too
			String paramValue = facesContext.getExternalContext().getRequestParameterMap().get("param1");

			if ((portletMode == null) || !portletMode.toString().equalsIgnoreCase("view")) {
				testBean.setTestResult(false,
					"encodeResourceURL incorrectly encoded the portlet mode.  The resulting request should have ignored the mode and remained in 'view' mode.");
			}
			else if (paramValue == null) {
				testBean.setTestResult(false,
					"encodeResourceURL incorrectly encoded the mode and parameter.  The resulting request didn't contain the expected 'param1' parameter.");
			}
			else if (!paramValue.equals("testValue")) {
				testBean.setTestResult(false,
					"encodeResourceURL incorrectly encoded the mode and parameter.  The resulting request contained the wrong parameter value. Expected: testValue  Received: " +
					paramValue);
			}
			else {
				testBean.setTestResult(true,
					"encodeResourceURL correctly encoded a URL by ignoring the mode and properly encoding the parameter.");
			}

			testBean.setTestComplete(true);

			if (testBean.getTestStatus()) {
				return Constants.TEST_SUCCESS;
			}
			else {
				return Constants.TEST_FAILED;
			}
		}
		else {
			return "";
		}
	}

	// Test is MultiRequest -- Render/Action
	// Test #6.136 (b)
	@BridgeTest(test = "encodeResourceURLWithWindowStateTest")
	public String encodeResourceURLWithWindowStateTest(TestBean testBean) {

		// This tests that we can encode a new mode in an actionURL
		// done by navigation rule.
		FacesContext facesContext = FacesContext.getCurrentInstance();

		if (BridgeUtil.getPortletRequestPhase(facesContext) == Bridge.PortletPhase.RESOURCE_PHASE) {
			PortletRequest portletRequest = (PortletRequest) facesContext.getExternalContext().getRequest();

			// Parameter/Mode encoded in the faces-config.xml target
			WindowState windowState = portletRequest.getWindowState();

			// Check that the parameter came along too
			String paramValue = facesContext.getExternalContext().getRequestParameterMap().get("param1");

			if ((windowState == null) || !windowState.toString().equalsIgnoreCase("normal")) {
				testBean.setTestResult(false,
					"encodeResourceURL incorrectly encoded the portlet window state.  The resulting request should have ignored the invalid window state and remained in 'normal' mode.");
			}
			else if (paramValue == null) {
				testBean.setTestResult(false,
					"encodeResourceURL incorrectly encoded the window state and parameter.  The resulting request didn't contain the expected 'param1' parameter.");
			}
			else if (!paramValue.equals("testValue")) {
				testBean.setTestResult(false,
					"encodeResourceURL incorrectly encoded the window state and parameter.  The resulting request contained the wrong parameter value. Expected: testValue  Received: " +
					paramValue);
			}
			else {
				testBean.setTestResult(true,
					"encodeResourceURL correctly encoded a URL by ignoring the window state and properly encoding the parameter.");
			}

			testBean.setTestComplete(true);

			if (testBean.getTestStatus()) {
				return Constants.TEST_SUCCESS;
			}
			else {
				return Constants.TEST_FAILED;
			}
		}
		else {
			return "";
		}
	}
}
