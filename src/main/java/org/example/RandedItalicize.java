package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.*;
import java.awt.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;

public class RandedItalicize {
    private static final String RANDEVU_URL = "https://webrandevu.medicine.ankara.edu.tr/#/";
    private static String oncekiDurum = "";
    // Removed unused field declaration
    public static void main(String[] args) {
        // SSL doğrulamasını devre dışı bırak
        disableSSLCertificateValidation();

            Timer timer = new Timer();
            timer.schedule(new RandevuKontrolGorevi(), 0, 300000); // 5 dakikada bir kontrol
    }

    static class RandevuKontrolGorevi extends TimerTask {

        @Override
        public void run() {
            try {
                Document doc = Jsoup.connect(RANDEVU_URL).get();
                String mevcutDurum = doc.select("hedef-element-selektoru").text();

                if (!mevcutDurum.equals(oncekiDurum)) {
                    // SMS gönder
                    SmsSender.sendSms("+90#####", "Yeni bir randevu açıldı!");
                    oncekiDurum = mevcutDurum;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


        private void bildirimGoster(String mesaj) throws AWTException {
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
                TrayIcon trayIcon = new TrayIcon(image, "Randevu İzleyici");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("Randevu İzleyici");
                tray.add(trayIcon);
                trayIcon.displayMessage("Randevu Bildirimi", mesaj, TrayIcon.MessageType.INFO);
            } else {
                System.err.println("Sistem tepsisi desteklenmiyor!");
            }
        }

    /**
     * SSL sertifika doğrulamasını devre dışı bırakır (güvenli bir ortamda kullanılmamalıdır).
     */
    private static void disableSSLCertificateValidation() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) { }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
