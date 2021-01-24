package mx.tecnm.tepic.ladm_u4_practica2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Toast

class smsReceiver : BroadcastReceiver(){
    var respuesta = ""
    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = intent!!.extras
        if(extras!=null){
            var sms = extras!!.get("pdus") as Array<Any>
            for(indice in sms.indices)
            {
                val formato = extras!!.getString("format")
                var smsMensaje = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    SmsMessage.createFromPdu(sms[indice] as ByteArray,formato)
                } else{
                    SmsMessage.createFromPdu(sms[indice] as ByteArray)
                }
                var celularOrigen = smsMensaje.originatingAddress
                var contenidoSMS = smsMensaje.messageBody.toString()
                var mensaje = contenidoSMS.split(" ")
                var selecc = mensaje[2]
                Toast.makeText(context,"Entro contenido ${contenidoSMS}",Toast.LENGTH_LONG).show()
                if(mensaje.size != 3){
                    SmsManager.getDefault().
                    sendTextMessage(celularOrigen,null,"" +
                            "Envie:" +
                            " PRODUCTO un espacio, el número de sucursal, espacio y el id del producto.\n " +
                            "Ejemplo: PRODUCTO 1 111",null,null) }
                else if(!mensaje[0].equals("PRODUCTO")){ SmsManager.getDefault().sendTextMessage(celularOrigen,null,
                        "Mensaje escrito de manera incorrecta.\n Seguir la siguiente sintaxis:PRODUCTO 1 111",
                    null,null) }
                else if 
                             (selecc.equals("1") || selecc.equals("2") || selecc.equals("3") || selecc.equals("4") || selecc.equals("5")){
                    try{
                        val cursor = BD(context, "bdP", null, 1)
                            .readableDatabase
                            .rawQuery("SELECT CANTIDAD FROM PRODUCTO_D WHERE TIENDA  = '${mensaje[2]}' AND ID_PRODUCTO ='${mensaje[1]}'",null)

                        if(cursor.moveToFirst()){
                            do { respuesta="La cantida del producto en esta sucursal: "+cursor.getString(0)
                                SmsManager.getDefault().
                                sendTextMessage(celularOrigen,null,respuesta,null,null)
                            }while(cursor.moveToNext())
                        }else{
                            SmsManager.getDefault().sendTextMessage(
                                celularOrigen, null, "No hay en existencia.", null, null
                            ) }
                    }catch(e:SQLiteException){
                        Toast.makeText(context, e.message,Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    SmsManager.getDefault().sendTextMessage(celularOrigen,null,
                        "La sucursal ingresada no existe, favor de verificar.",null,null)
                }


            }
        }
    }

}