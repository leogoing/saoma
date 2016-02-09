package interceptors;

import com.jfinal.render.FreeMarkerRender;

public class MyErrorRender extends FreeMarkerRender {

	public MyErrorRender(String view) {
		super(view);
		try {
			super.render();
		} catch (Exception e) {
//			Writer w = getResponse().getWriter();
//			w.write(e.getMessage());
		}
	}

}
