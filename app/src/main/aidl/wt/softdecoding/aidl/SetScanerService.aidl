// ScanerSetService.aidl
package wt.softdecoding.aidl;
import wt.softdecoding.aidl.ScanerListener;
// Declare any non-default types here with import statements

interface SetScanerService {

     void setCaseconversion(int caseconversion);
     void SetSupplementalLightBrightness(boolean isOpen);
     void setGoodReadBeeper(boolean isOpen);
     void setPresentationModeRereadDelay(int presentationModeRereadDelay);
     void setAddCRSuffix(boolean isOpen);
     void SetPrefix(boolean isOpen,String text);
     void SetSuffix(boolean isOpen,String text);
     void SetUPCAToEAN13(boolean isOpen);
     void SetBlockURLs(boolean isOpen);
     void setAllSymbologies(boolean isOpen);
     void setCode39min(int minNum);
     void setCode39max(int maxNum);
     void SetSelectSymbologies(int decoder,boolean isOpen);
     void StartAutomaticOutput();
     void StopAutomaticOutput();
     void SetScanrListener(ScanerListener listener);


}