package mx.tecnm.tepic.ladm_u4_practica2

import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val permmensaje = 1
    val permrecibir = 2
    val permlectura = 3

    var baseDatos=BD(this,"bdP", null, 1)
    var listaID=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.RECEIVE_SMS),permrecibir)
        }
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_SMS),permlectura)
        }
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.SEND_SMS), permmensaje
            )
        }
        mostrar_productos()
        btn1.setOnClickListener {
            insertar()
        }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) { super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==permmensaje){ }

        if(requestCode==permrecibir){ }

        if(requestCode==permlectura){ } }

    private fun mostrar_productos() {
        try{
            var trans=baseDatos.readableDatabase
            var prd_l=ArrayList<String>()
            var respuesta=trans.query("PRODUCTO_D", arrayOf("*"),null,null,null,null,null)
            listaID.clear()
            if (respuesta.moveToFirst()){
                do{
                    var concatenacion=
                        "Producto: ${respuesta.getString(0)}\nSucursal: ${respuesta.getString(1)}\nCantidad: ${respuesta.getString(2)}"
                    prd_l.add(concatenacion)
                }while (respuesta.moveToNext())

            }else{
                prd_l.add("No se han ingresado productos.")
            }
            lista.adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,prd_l)
            trans.close()
        }catch (e: SQLiteException){mensaje("ERROR: "+e.message!!)}
    }
    fun mensaje(men:String){
        AlertDialog.Builder(this).setMessage(men).show()
    }
    private fun insertar() {
        var prod = txtProducto.text.toString()
        var suc = txtNSuc.text.toString()
        var cantidad = txtCantidad.text.toString().toInt()
        txtCantidad.setText("")
        txtNSuc.setText("")
        txtProducto.setText("")
        try{
            var bd = BD(this, "bdP", null, 1)
            var insertar = baseDatos.writableDatabase
            var SQL = "INSERT INTO PRODUCTO_D VALUES(" +
                    "'${prod}','${suc}',${cantidad})"
            insertar.execSQL(SQL)
            baseDatos.close()
            mostrar_productos()
        }catch (error: SQLiteException){
            mensaje(error.message.toString()) }
    }
}