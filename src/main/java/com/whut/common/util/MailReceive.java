package com.whut.common.util;

import java.io.*;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import sun.misc.BASE64Decoder;

public class MailReceive {
    
    public MailReceive(){
        
    }

    public static void main(String[] args) {
        MailReceive receive = new MailReceive();
        receive.getMails(10,"1365733349@qq.com","kvqlngmmqtekjjbi");
    }
    
    private Folder inbox = null;
    
    private String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory"; 
    
    //获取所有邮件
    public String getMails(int page,String username,String pass){
        int pageNum = 10;
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Properties props = System.getProperties();
        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.store.protocol", "imap"); 
        props.setProperty("mail.imap.host", "imap.exmail.qq.com"); 
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.socketFactory.port", "993");
        
        Session session = Session.getDefaultInstance(props,null);
        
        URLName urln = new URLName("imap","imap.exmail.qq.com",993,null,
                username, pass);
        
        Store store = null;
        String msg = "";
        
        try {
            store = session.getStore(urln);
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            FetchProfile profile = new FetchProfile();
            profile.add(FetchProfile.Item.ENVELOPE);
            Message[] messages = inbox.getMessages();
            inbox.fetch(messages, profile);
            
            //计算页数
            int all = inbox.getMessageCount();
            int pages = all/pageNum;
            if(all%pageNum>0){
                pages++;
            }
            int begin = all-((page-1)*pageNum);
            
            
            
            for (int i = begin-1; i > (begin-pageNum)&&i>=0; i--) {
                //邮件发送者
                String from = decodeText(messages[i].getFrom()[0].toString());
                msg += messages[i].getMessageNumber()+"&";
                InternetAddress ia = new InternetAddress(from);
                msg+=ia.getPersonal()+"&";
                msg+=ia.getAddress()+"&";
                //邮件标题
                msg+=messages[i].getSubject()+"&";
                //邮件大小
                msg+=messages[i].getSize()+"&";
                //邮件发送时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                msg+=format.format(messages[i].getSentDate())+";";
                
                //判断是否由附件
//                if(messages[i].isMimeType("multipart/*")){
//                    Multipart multipart = (Multipart)messages[i].getContent();
//                    int bodyCounts = multipart.getCount();
//                    for(int j = 0; j < bodyCounts; j++) {
//                        BodyPart bodypart = multipart.getBodyPart(j);
//                        if(bodypart.getFileName() != null){
//                            String filename = bodypart.getFileName(); 
//                            if(filename.startsWith("=?")){
//                                filename = MimeUtility.decodeText(filename);
//                            }
//                            msg+=filename+"<br/>";
//                        }
//                    }
//                }
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        } finally{
            try {
                inbox.close(false);
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }finally{
                return msg;
            }
        }
    }
    
    protected static String decodeText(String text) throws UnsupportedEncodingException{
        if (text == null){
            return null;
        }
        if (text.startsWith("=?GB") || text.startsWith("=?gb")){
            text = MimeUtility.decodeText(text);
        }else{
            text = new String(text.getBytes("ISO8859_1"));
        }
        return text;
    }
    
    
    //附件下载到服务器
    public void getAtth(int msgnum,int bodynum,String filename,String mailpath) throws MessagingException, IOException{
        Message message = inbox.getMessage(msgnum);
        Multipart multipart = (Multipart)message.getContent(); 
        BodyPart bodypart = multipart.getBodyPart(bodynum); 
        InputStream input = bodypart.getInputStream();
        byte[] buffer = new byte[input.available()];
        input.read(buffer);
        input.close();
        
        File file = new File("D:\\App\\MailFile\\"+mailpath);
        if(!file.isDirectory()&&!file.exists()){
            file.mkdir();
        }
        
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(buffer);
        fos.close();
    }
    
    //获得单个邮件信息
    public String getMail(int id,final String username,final String pass) throws MessagingException, IOException{
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Properties props = System.getProperties();
        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.store.protocol", "imap"); 
        props.setProperty("mail.imap.host", "imap.exmail.qq.com"); 
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.socketFactory.port", "993");
        
        Session session = Session.getDefaultInstance(props,null);
        
        URLName urln = new URLName("imap","imap.exmail.qq.com",993,null,
                username, pass);
        
        Store store = session.getStore(urln);
        store.connect();
        inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        FetchProfile profile = new FetchProfile();
        profile.add(FetchProfile.Item.ENVELOPE);
        
        Message message = inbox.getMessage(id);
        
        String msg = "";
        
        msg+=message.getMessageNumber()+"&#@";
        msg+=message.getSubject()+"&#@";
        msg+=new InternetAddress(message.getFrom()[0].toString()).getAddress()+"&#@";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msg+=format.format(message.getSentDate())+"&#@";
        
        //判断是否由附件
        if(message.isMimeType("multipart/*")){
            Multipart multipart = (Multipart)message.getContent();
            int bodyCounts = multipart.getCount();
            for(int j = 0; j < bodyCounts; j++) {
                BodyPart bodypart = multipart.getBodyPart(j);
                if(bodypart.getContent()!=null){
                    msg+=bodypart.getContent()+"&#@";
                }
            }
        }else{
            msg+=message.getContent().toString()+"&#@";
        }
        
        if(message.isMimeType("multipart/*")){
            Multipart multipart = (Multipart)message.getContent();
            int bodyCounts = multipart.getCount();
            for(int j = 0; j < bodyCounts; j++) {
                BodyPart bodypart = multipart.getBodyPart(j);
                if(bodypart.getFileName() != null){
                    String filename = bodypart.getFileName(); 
                    if(filename.startsWith("=?")){
                        filename = MimeUtility.decodeText(filename);
                    }
                    msg+=filename+";";
                }
            }
        }
        
        store.close();
        return msg;
    }
    
    //发送邮件
    public String sendmail(final String username,final String pass,String from,String to,String subject,String content){
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.store.protocol", "smtp"); 
        props.setProperty("mail.smtp.host", "smtp.exmail.qq.com"); 
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, pass);
            }});
        
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to,false));
            msg.setSubject(subject);
            msg.setText(content);
            msg.setSentDate(new Date());
            Transport.send(msg);
            return "1";
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            return e.getMessage();
        }finally{
            
        }
    }
    
    //Base64
    public static String decryptBASE64(String key) throws Exception {    
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bt = decoder.decodeBuffer(key);
        return new String(bt,"GBK");
    }
    
    //新邮件提醒
    public int MailMsg(String username,String pass){
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Properties props = System.getProperties();
        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.store.protocol", "imap"); 
        props.setProperty("mail.imap.host", "imap.exmail.qq.com"); 
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.socketFactory.port", "993");
        
        Session session = Session.getDefaultInstance(props,null);
        
        URLName urln = new URLName("imap","imap.exmail.qq.com",993,null,
                username, pass);
        
        Store store = null;
        int num = 0;
        
        try {
            store = session.getStore(urln);
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            FetchProfile profile = new FetchProfile();
            profile.add(FetchProfile.Item.ENVELOPE);
            
            num = inbox.getMessageCount();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            num = 0;
        }finally{
            try {
                inbox.close(false);
                store.close();
            } catch (MessagingException e) {
                // TODO Auto-generated catch block
            }finally{
                return num;
            }
        }        
    }
}