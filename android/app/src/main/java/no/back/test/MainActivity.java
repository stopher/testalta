package no.back.test;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.getcapacitor.Bridge;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginHandle;
import com.getcapacitor.ui.Toast;

import java.util.ArrayList;

public class MainActivity extends BridgeActivity {
  NfcAdapter nfcAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the Bridge

    Bridge bridge = this.getBridge();
    
    this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
      // Additional plugins you've installed go here
      // Ex: add(TotallyAwesomePlugin.class);

      add(NfcUri.class);
    }});

    nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    if (nfcAdapter == null) {
      Toast.show(this, "Feil. NFC mangler på denne enheten! (nfcAdapter is null)");
      finish();
      return;
    }

    if (!nfcAdaptber.isEnabled()) {
      Toast.show(this, "Feil ved NFC. nfcAdapter.isEnabled === false.");
    }

    handleIntent(getIntent());
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    handleIntent(intent);
  }

  private void handleIntent(Intent intent) {
    if (intent == null) return;
    if (intent.getAction() != NfcAdapter.ACTION_NDEF_DISCOVERED) return;

    Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
    NdefMessage ndefMsg = (NdefMessage) rawMessages[0];
    NdefRecord ndefRecord = ndefMsg.getRecords()[0];

    if (ndefRecord.toUri() == null) return;
    String uri = ndefRecord.toUri().toString();
    Log.i("NFC URI", ndefRecord.toUri().toString());
    Toast.show(this, "NFC URI" + ndefRecord.toUri().toString());

    PluginHandle pluginHandle = bridge.getPlugin("NfcUri");
    if (pluginHandle == null) return;
    NfcUri nfcUri = (NfcUri) pluginHandle.getInstance();
    if (nfcUri == null) return;

    nfcUri.setLastReadNfcUri(uri);
  }


}
