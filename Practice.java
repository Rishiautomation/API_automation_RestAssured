import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static  io.restassured.RestAssured.*;
import  static org.hamcrest.Matchers.*;

import java.io.File;

import Files.Payload;

public class Practice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
  
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		String response=given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(Payload.AddPlace()).when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		
		
		//Add Place-> Update the place with New Address->Get Place to validate if new response is present or not.
		//This is end to end.
	
		 String newaddress="Summer walk ,Africa";
		System.out.println(response);
		JsonPath js = new JsonPath(response);//For pasrsing the JSON (Add the place)
		String placeid=js.getString("place_id");
		System.out.println(placeid);
		
		//Update the place with new address.
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeid+"\",\r\n"
				+ "\"address\":\""+newaddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}").when().put("/maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		
		//get place
		String getResponse=given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeid)
				.when().get("maps/api/place/get/json")
				.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		 JsonPath js1=new JsonPath(getResponse);
		 String actualaddress=js1.getString("address");
		 System.out.println(actualaddress);
	}

}
