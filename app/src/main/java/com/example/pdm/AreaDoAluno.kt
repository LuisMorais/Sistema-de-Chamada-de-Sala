package com.example.pdm

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class AreaDoAluno : AppCompatActivity() {

    var permissoesRequeridas = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val APP_PERMISSOES_ID = 2022

    var txtValorLatitude: TextView? = null
    var txtValorLongitude: TextView? = null

    var latitude: Double = 0.00
    var longitude: Double = 0.00

    var locationManager: LocationManager? = null
    var gpsAtivo = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_do_aluno)


        // Recuperando os parâmetros passados
        val dados = intent
        val parametros = dados.extras
        val sNome = parametros!!.getString("ra")

        // Atualizando a tela
        val txtNome: TextView = findViewById(R.id.raAluno)
        txtNome.text = "RA: $sNome"


        val date = Calendar.getInstance().time
        var horario = SimpleDateFormat("HH:mm", Locale.getDefault()) //Horario atual
        var data = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) //Dia atual
        var diaDaSemana = SimpleDateFormat("EEE", Locale.getDefault()) //Dia da semana atual
        val diaLogin: TextView = findViewById(R.id.diaLogin)
        diaLogin.text = "Login feito no dia " + data.format(date) + " às " + horario.format(date)


        var arrayMaterias = arrayOf(
            "TRABALHO DE GRADUAÇÃO INTERDISCIPLINAR I",
            "MATÉRIA TESTE TERÇA",
            "PROGRAMAÇÃO PARA DISPOSITIVOS MÓVEIS",
            "FUNDAMENTOS DE INTELIGÊNCIA ARTIFICIAL",
            "LINGUAGENS FORMAIS E AUTÔMATOS"
        )
        //Matérias


        var materiaDoDia: TextView = findViewById(R.id.materiaDoDia)
        var situacaoAula: TextView = findViewById(R.id.situacaoAula)
        var presenca: TextView = findViewById(R.id.presenca)
        val btnPresenca: Button = findViewById(R.id.btnPresenca)

        var horarioAulaInicio =
            SimpleDateFormat("19:10", Locale.getDefault()) //Horário da aula 19:10 as 21:50
        var horarioAulaFim =
            SimpleDateFormat("21:50", Locale.getDefault()) //Horário da aula 19:10 as 21:50
        var registro = false
        var numMateria = 0


        if (diaDaSemana.format(date) == "Mon") {
            materiaDoDia.text = arrayMaterias[0]
            numMateria = 0
        }
        if (diaDaSemana.format(date) == "Tue") {
            materiaDoDia.text = arrayMaterias[1]
            numMateria = 1
        }
        if (diaDaSemana.format(date) == "Wed") {
            materiaDoDia.text = arrayMaterias[2]
            numMateria = 2
        }
        if (diaDaSemana.format(date) == "Thu") {
            materiaDoDia.text = arrayMaterias[3]
            numMateria = 3
        }
        if (diaDaSemana.format(date) == "Fri") {
            materiaDoDia.text = arrayMaterias[4]
            numMateria = 4
        }
        if (diaDaSemana.format(date) == "Sat") {
            materiaDoDia.text = "Hoje é sábado, dia de descansar!"
        }
        if (diaDaSemana.format(date) == "Sun") {
            materiaDoDia.text = "Hoje é domingo, dia de descansar!"
        }

         var latitudeUnicid = -23.5361
         var longitudeUnicid = -46.5594
        if ((horario.format(date) >= horarioAulaInicio.format(date)) && (horario.format(date) <= horarioAulaFim.format(date)) && (registro == false)
        ) {
            situacaoAula.text = "Sua aula começou, boa aula! Você tem até as 21:50 para registrar sua presença!"
            presenca.text = "Presença: DISPONÍVEL"


            txtValorLatitude = findViewById(R.id.txtValorLatitude)
            txtValorLongitude = findViewById(R.id.txtValorLongitude)

            locationManager =
                application.getSystemService(LOCATION_SERVICE) as LocationManager


            gpsAtivo = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (gpsAtivo) {
                obterCoordenadas()
            } else {
                latitude = 0.00
                longitude = 0.00

                txtValorLatitude!!.setText(formatarGeopoint(latitude))
                txtValorLongitude!!.setText(formatarGeopoint(longitude))

                Toast.makeText(
                    this,
                    "Coordenadas não Disponíveis", Toast.LENGTH_LONG
                ).show()
            }
            if(((formatarGeopoint(latitude)) == formatarGeopoint(latitudeUnicid)) && (formatarGeopoint(longitude) == formatarGeopoint(longitudeUnicid))){
            btnPresenca.setOnClickListener {
                situacaoAula.text = "Localização: 21312312, 43242344"
                presenca.text = "Presença: REGISTRADA - Às: " + horario.format(date)
                registro = true
                Toast.makeText(
                    applicationContext,
                    "Presença registrada: " + arrayMaterias[numMateria] + " - Dia: " + data.format(
                        date
                    ) + " às " + horario.format(date) +
                            " - Localização:"+latitude+", "+longitude,
                    Toast.LENGTH_LONG
                ).show()
                btnPresenca.setVisibility(View.INVISIBLE)
            }}else{

            }
        } else {
            btnPresenca.setVisibility(View.INVISIBLE)
        }


        val btnCronograma: Button = findViewById(R.id.btnCronograma)
        btnCronograma.setOnClickListener {
            val telaCronogramaDeAulas = Intent(this, CronogramaDeAulas::class.java)
            startActivity(telaCronogramaDeAulas)
            Toast.makeText(
                applicationContext,
                "Indo para o cronograma de aulas!",
                Toast.LENGTH_LONG
            ).show()
        }

        val btnDeslogar: Button = findViewById(R.id.btnDeslogar)
        btnDeslogar.setOnClickListener {
            Toast.makeText(applicationContext, "Deslogado com sucesso!", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    private fun obterCoordenadas() {

        val permissaoAtiva: Boolean =
            solicitarPermissaoParaObterLocalizacao()

        if (permissaoAtiva)
            capturarUltimaLocalizacaoValida()


    }

    private fun solicitarPermissaoParaObterLocalizacao(): Boolean {
        Toast.makeText(
            this,
            "Verificando permissões....",
            Toast.LENGTH_LONG
        ).show()

        val permissoesNegadas: MutableList<String> =
            ArrayList()

        var permissaoNegada: Int

        // permissoesRequeridas
        for (permissao in permissoesRequeridas) {

            permissaoNegada = ContextCompat.checkSelfPermission(
                this@AreaDoAluno, permissao
            )

            if (permissaoNegada != PackageManager.PERMISSION_GRANTED) {
                permissoesNegadas.add(permissao)
            }

        }

        return if (!permissoesNegadas.isEmpty()) {

            ActivityCompat.requestPermissions(
                this@AreaDoAluno,
                permissoesNegadas.toTypedArray(),
                APP_PERMISSOES_ID
            )
            false
        } else {
            true
        }
    }

    private fun capturarUltimaLocalizacaoValida() {
        @SuppressLint("MissingPermission")
        val location = locationManager!!.getLastKnownLocation(
            LocationManager.GPS_PROVIDER
        )

        if (location != null) {

            latitude = location.latitude
            longitude = location.longitude

        } else {
            latitude = 0.00
            longitude = 0.00
        }

        txtValorLatitude!!.setText(formatarGeopoint(latitude))
        txtValorLongitude!!.setText(formatarGeopoint(longitude))

        Toast.makeText(
            this,
            "Coordenadas obtidas com sucesso", Toast.LENGTH_LONG
        ).show()


    }

    private fun formatarGeopoint(valor: Double): String? {

        val decimalFormat = DecimalFormat("#.####")

        return decimalFormat.format(valor)
    }
}







