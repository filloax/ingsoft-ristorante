package it.unibo.ingsoft.fortuna;

//import java.io.Serializable;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloJsonController {

	@GetMapping("/hello_json")
	public TestObject hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new TestObject(15, name);
	}

	private class TestObject {
		private int field1;
		private String field2;

		public TestObject(int field1, String field2) {
			this.field1 = field1;
			this.field2 = field2;
		}

		public int getField1() { return field1; }
		public String getField2() { return field2; }
	}
}
