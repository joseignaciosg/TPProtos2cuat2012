public class MailTransformer {

    public void transformHeader(MimeHeader header) throws IOException {
        // Mime header transformation
    }
    
    public StringBuilder transformPart(Map<String, MimeHeader> partHeaders, StringBuilder part) throws IOException {
        // Mime part transformation
    }
    
    public void transformComplete(Mail mail) {
        // Full mail transformation (external apps)
    }
}
