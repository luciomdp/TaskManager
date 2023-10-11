package com.advenio.medere.emr.dao.dto;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.Transient;
import javax.crypto.spec.SecretKeySpec;
import com.advenio.medere.sender.objects.dto.MessageDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrevScheduledMessageDTO {

    private String webappointmentsurl; 
    private BigInteger patientcarequeue; 
    private BigInteger patientid; 
    private String bodymessage; 
    private String subject;
    private Timestamp datetosend;
    private String _to;

    @Transient
    private static Cipher cipher;

    @Transient
    public MessageDTO transform() {
        
        StringBuilder cancelLink = new StringBuilder(webappointmentsurl + "cancelAppointment")
		.append("?appID=" + encrypt(patientcarequeue.toString()))
		.append("&me=" + encrypt(patientid.toString()));

        MessageDTO m = new MessageDTO();
        m.setBodymessage(bodymessage.replace("[HREFBUTTON]", cancelLink));
        m.setCalendar(null);
        m.setContenttype(BigInteger.valueOf(1));
        m.setDatetosend(datetosend.toLocalDateTime());
        m.setFiles(new ArrayList<>());
        m.setHasCalendar(false);
        m.setHasattachments(false);
        m.setMessagetype(BigInteger.valueOf(1));
        m.setSubject(subject);
        m.set_to(_to);
        return m;
    }

    @Transient
    public static String encrypt(final String data) {
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec key = new SecretKeySpec(Arrays.copyOf(
                new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
                0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f}, 16), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeBase64URLSafeString(cipher.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Error occured while encrypting data", e);
        }
    }
}
