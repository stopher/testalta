package no.back.test;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.PluginResult;

@NativePlugin
public class NfcUri extends Plugin {
    private String uri;

    /*
        Usage

        Capacitor.Plugins.NfcUri.getLast().then((value: NfcUriObject) => {
            console.log(value);
        });
     */

    @PluginMethod()
    public void getLast(PluginCall call) {
        MainActivity mainActivity = (MainActivity) this.getActivity();

        call.resolve(getNfcUriObject(uri));
    }


    // To set by MainActivity when NFC is rea
    public void setLastReadNfcUri(String uri) {
        this.uri = uri;

        /*
          To listen for nfc
          Capacitor.Plugins.NfcUri.addListener('nfcRead', (event: NfcUriObject) => {
            console.log(event.uri);
          );
        */

        notifyListeners("onRead", getNfcUriObject(uri));
    }

    /*
        TypeScript:
        interface NfcUriObject { uri?: string }
     * */

    private JSObject getNfcUriObject(String uri) {
        JSObject object = new JSObject();
        if (uri != null) {
            object.put("uri", uri);
        }
        return object;
    }

}
