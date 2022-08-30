	package com.example.sampleauth;

	import io.swagger.v3.oas.annotations.Operation;
	import io.swagger.v3.oas.annotations.security.SecurityRequirement;
	import org.apache.catalina.filters.ExpiresFilter;
	import org.springframework.beans.factory.annotation.Value;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
	import org.springframework.security.core.context.SecurityContext;
	import org.springframework.security.core.context.SecurityContextHolder;
	import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RestController;

	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import java.io.IOException;
	import java.net.InetAddress;
	import java.net.UnknownHostException;
	import java.security.Principal;

	@SpringBootApplication()
	public class SampleAuthApplication {

		public static void main(String[] args) {
			SpringApplication.run(SampleAuthApplication.class, args);
		}

	}

	@RestController
	class TestController {

		@Value("${CALLBACK_URL:http://localhost:8080}")
		private String callBackUrl;

		@GetMapping("/login")
		public void login(HttpServletResponse httpServletResponse) throws IOException {
			httpServletResponse.sendRedirect(
					"https://dev-uy72e6f5.us.auth0.com/authorize?response_type=token&client_id=bxWqixadNafkm75TqqBah05Ej8fIrI9J&redirect_uri="+callBackUrl+"/callback.html&scope=openid%20profile%20email&&audience=https://dev-uy72e6f5.us.auth0.com/api/v2/"
			);
		}

		@GetMapping("/test1")
		@Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
		public String test1() throws UnknownHostException {
			InetAddress IP=InetAddress.getLocalHost();
 			return "application is up and run "+ IP.toString();
		}


		@GetMapping("/test")
		@Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
		public String test(HttpServletRequest request) {
			JwtAuthenticationToken obj  =(JwtAuthenticationToken) request.getUserPrincipal();
			String email = obj.getToken().getClaim("http://mibrahim.com/email");
			return "its working fine user is " + obj.getName();
		}

	}