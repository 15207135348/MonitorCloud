package com.whut.common.util;

import com.sun.mail.util.MailSSLSocketFactory;

import java.util.Properties;

import javax.mail.*;

class MailReceives {
    public static void main(String[] args) throws Exception {
        // 定义连接POP3服务器的属性信息
        String pop3Server = "pop.qq.com";
        String protocol = "pop3";
        String username = "1365733349@qq.com";
        String password = "kvqlngmmqtekjjbi"; // QQ邮箱的SMTP的授权码，什么是授权码，它又是如何设置？

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", protocol); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.pop3.host", pop3Server);
        props.setProperty("mail.pop3.port", "110");
        props.setProperty("mail.auth", "true");
        props.setProperty("mail.username","1365733349@qq.com");
        props.setProperty("mail.password","kvqlngmmqtekjjbi");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.pop3.ssl.enable","true");
        props.put("mail.pop3.ssl.socketFactory",sf);

        // 获取连接
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        Session session = Session.getDefaultInstance(props,auth);
        session.setDebug(false);

        // 获取Store对象
        Store store = session.getStore(protocol);
        store.connect(pop3Server, username, password); // POP3服务器的登陆认证

        // 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
        Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户
        folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限

        int count = folder.getNewMessageCount();
        Message[] messages = folder.getMessages();// 得到邮箱帐户中的所有邮件

        for (Message message : messages) {
            String subject = message.getSubject();// 获得邮件主题
            Address from = (Address) message.getFrom()[0];// 获得发送者地址
            System.out.println("邮件的主题为: " + subject + "\t发件人地址为: " + from);
            System.out.println("邮件的内容为：");
            message.writeTo(System.out);// 输出邮件内容到控制台
        }

        folder.close(false);// 关闭邮件夹对象
        store.close(); // 关闭连接对象
    }
}