package kereros.get;
import java.io.IOException;
import java.security.Principal;
import java.security.PrivilegedAction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


import javax.security.auth.Subject;

import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.SPNegoSchemeFactory;

import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author richer.sun
 * @description http请求 带有kerberos认证的集群
 * @date 2021/9/15 6:00 下午
 */
public class RequestKerberosUrlUtils {
    public static Logger logger = LoggerFactory.getLogger(RequestKerberosUrlUtils.class);
    private String principal;
    private String keyTabLocation;

    public RequestKerberosUrlUtils() {
    }

    public RequestKerberosUrlUtils(String principal, String keyTabLocation) {
        super();
        this.principal = principal;
        this.keyTabLocation = keyTabLocation;
    }

    public RequestKerberosUrlUtils(String principal, String keyTabLocation, boolean isDebug) {
        this(principal, keyTabLocation);
        if (isDebug) {
            System.setProperty("sun.security.spnego.debug", "true");
            System.setProperty("sun.security.krb5.debug", "true");
        }
    }

    public RequestKerberosUrlUtils(String principal, String keyTabLocation, String krb5Location, boolean isDebug) {
        this(principal, keyTabLocation, isDebug);
        System.setProperty("java.security.krb5.conf", krb5Location);
    }

    //模拟curl使用kerberos认证
    private static HttpClient buildSpengoHttpClient() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create().
                register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory(true)).build();
        builder.setDefaultAuthSchemeRegistry(authSchemeRegistry);
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(null, -1, null), new Credentials() {
            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }
        });
        builder.setDefaultCredentialsProvider(credentialsProvider);
        CloseableHttpClient httpClient = builder.build();
        return httpClient;
    }

    public HttpResponse callRestUrl(final String url, final String userId) {
        logger.warn(String.format("Calling KerberosHttpClient %s %s %s", this.principal, this.keyTabLocation, url));
        Configuration config = new Configuration() {
            @SuppressWarnings("serial")
            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
                return new AppConfigurationEntry[]{new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule",
                        AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, new HashMap<String, Object>() {
                    {
                        put("useTicketCache", "false");
                        put("useKeyTab", "true");
                        put("keyTab", keyTabLocation);
                        //Krb5 in GSS API needs to be refreshed so it does not throw the error
                        //Specified version of key is not available
                        put("refreshKrb5Config", "true");
                        put("principal", principal);
                        put("storeKey", "true");
                        put("doNotPrompt", "true");
                        put("isInitiator", "true");
                        put("debug", "true");
                    }
                })};
            }
        };
        Set<Principal> princ = new HashSet<Principal>(1);
        princ.add(new KerberosPrincipal(userId));
        Subject sub = new Subject(false, princ, new HashSet<Object>(), new HashSet<Object>());
        try {
            //认证模块：Krb5Login
            LoginContext lc = new LoginContext("Krb5Login", sub, null, config);
            lc.login();
            Subject serviceSubject = lc.getSubject();
            return Subject.doAs(serviceSubject, new PrivilegedAction<HttpResponse>() {
                HttpResponse httpResponse = null;

                @Override
                public HttpResponse run() {
                    try {
                        HttpUriRequest request = new HttpGet(url);

                        HttpClient spnegoHttpClient = buildSpengoHttpClient();
                        httpResponse = spnegoHttpClient.execute(request);
                        return httpResponse;
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return httpResponse;
                }
            });
        } catch (Exception le) {
            le.printStackTrace();
        }
        return null;
    }

    public HttpResponse callRestUrl_put(final String url, final String userId) {
        logger.warn(String.format("Calling KerberosHttpClient %s %s %s", this.principal, this.keyTabLocation, url));
        Configuration config = new Configuration() {
            @SuppressWarnings("serial")
            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
                return new AppConfigurationEntry[]{new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule",
                        AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, new HashMap<String, Object>() {
                    {
                        put("useTicketCache", "false");
                        put("useKeyTab", "true");
                        put("keyTab", keyTabLocation);
                        //Krb5 in GSS API needs to be refreshed so it does not throw the error
                        //Specified version of key is not available
                        put("refreshKrb5Config", "true");
                        put("principal", principal);
                        put("storeKey", "true");
                        put("doNotPrompt", "true");
                        put("isInitiator", "true");
                        put("debug", "true");
                    }
                })};
            }
        };
        Set<Principal> princ = new HashSet<Principal>(1);
        princ.add(new KerberosPrincipal(userId));
        Subject sub = new Subject(false, princ, new HashSet<Object>(), new HashSet<Object>());
        try {
            //认证模块：Krb5Login
            LoginContext lc = new LoginContext("Krb5Login", sub, null, config);
            lc.login();
            Subject serviceSubject = lc.getSubject();
            return Subject.doAs(serviceSubject, new PrivilegedAction<HttpResponse>() {
                HttpResponse httpResponse = null;

                @Override
                public HttpResponse run() {
                    try {
                        HttpPut httpPut = new HttpPut(url);
                        httpPut.addHeader("username","root/cloudwise-01@CLOUDWISE.COM");
                        JSONObject jsonObject = new JSONObject();
                        //jsonObject.put("state", "KILLED");
                        StringEntity stringEntity = new StringEntity(jsonObject.toString(),"UTF-8");
                        System.out.println("---------------------------");
                        stringEntity.setContentType("application/json");
                        httpPut.setEntity(stringEntity);
                        System.out.println("request"+ httpPut.getMethod() + "  " + httpPut.getURI());
                        HttpClient spnegoHttpClient = buildSpengoHttpClient();
                        httpResponse = spnegoHttpClient.execute(httpPut);
                        return httpResponse;
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return httpResponse;
                }
            });
        } catch (Exception le) {
            le.printStackTrace();
        }
        return null;
    }
}