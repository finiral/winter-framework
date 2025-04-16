package mg.itu.prom16.object;

public class Export {
    private String contentType; 
    private byte[] bytes;
    private String fileName;
    private String extension;
    public String getExtension() {
        return ".pdf";
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public Export() {
    }
    public Export(String contentType, byte[] bytes, String fileName) {
        setContentType(contentType);
        setFileName(fileName);
        setBytes(bytes);
    }


    
}
