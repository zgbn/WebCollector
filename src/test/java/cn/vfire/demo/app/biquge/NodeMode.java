package cn.vfire.demo.app.biquge;

import lombok.Getter;
import lombok.Setter;

public class NodeMode {

	@Getter
	@Setter
	private int idx = -1;

	@Getter
	@Setter
	private String name;
	
	@Getter
	@Setter
	private String info ;
	
	@Getter
	@Setter
	private String classify ;

	@Getter
	@Setter
	private String link;


	@Override
	public String toString() {
		return String.format("%d.%s{link:%s}", this.idx, this.name, this.link);
	}


	@Override
	public boolean equals(Object obj) {

		if (this.name == null) { return false; }

		if (this.name.equals(obj)) { return true; }

		if (obj instanceof String) {
			if (this.name.equalsIgnoreCase((String) obj)) { return true; }
		}

		return false;
	}

}
