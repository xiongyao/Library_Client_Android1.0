package Tool;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.PostMethod;

public class UTF8PostMethod extends PostMethod {
	 /** Creates a new instance of UTF8PostMethod */
    public UTF8PostMethod(String string) {
        super(string);
    }
    
    protected String getContentCharSet(Header contentheader) {
        return "UTF-8";
    }

}
