package init.wek.freeCode;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class CopyText {
    
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Texto copiado", text);
        clipboard.setPrimaryClip(clip);
    }
}
