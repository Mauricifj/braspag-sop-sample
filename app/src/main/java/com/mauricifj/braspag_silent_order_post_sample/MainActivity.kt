package com.mauricifj.braspag_silent_order_post_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import br.com.braspag.silentorderpost.SilentOrderPost
import br.com.braspag.silentorderpost.models.SilentOrderPostEnvironment
import br.com.braspag.silentorderpost.models.SilentOrderPostRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sop = SilentOrderPost(
                merchantId = "YOUR-MERCHANT-ID",
                environment = SilentOrderPostEnvironment.SANDBOX
        )

        sop.binQuery = true
        sop.verifyCard = true
        sop.tokenize = true

        sop.sendCardData(
                request = SilentOrderPostRequest(
                        cardHolder = "Maurici Ferreira Junior",
                        cardNumber = "4111111111111111",
                        cardExpirationDate = "01/2030",
                        cardSecurityCode = "123"
                )
        ) {
            if (it.validations?.modelState != null) {
                with(it.validations?.modelState!!) {
                    holder_validation_list.adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            holderName ?: listOf()
                    )

                    card_number_validation_list.adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            cardNumber ?: listOf()
                    )

                    expiration_validation_list.adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            expiration ?: listOf()
                    )

                    security_code_validation_list.adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            securityCode ?: listOf()
                    )

                    bin_query_validation_list.adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            binQuery ?: listOf()
                    )

                    verify_card_validation_list.adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            verifyCard ?: listOf()
                    )

                    tokenize_validation_list.adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            tokenize ?: listOf()
                    )
                }

                result_content.visibility = View.GONE
                error_content.visibility = View.GONE
                validation_content.visibility = View.VISIBLE

            } else if (it.errors != null) {
                error_list.adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        it.errors ?: listOf()
                )

                result_content.visibility = View.GONE
                validation_content.visibility = View.GONE
                error_content.visibility = View.VISIBLE

            } else if (it.result != null) {
                with(it.result!!) {
                    if (sop.tokenize) {
                        card_token_result.text = cardToken

                        payment_token_content.visibility = View.GONE
                        card_token_content.visibility = View.VISIBLE

                    } else {
                        payment_token_result.text = paymentToken

                        card_token_content.visibility = View.GONE
                        payment_token_content.visibility = View.VISIBLE
                    }

                    if (sop.binQuery) {
                        brand_result.text = brand
                        foreign_card_result.text = foreignCard.toString()
                        bin_query_return_code_result.text = binQueryReturnCode
                        bin_query_return_message_result.text = binQueryReturnMessage
                        card_bin_result.text = cardBin
                        card_last_four_digits_result.text = cardLast4Digits

                        bin_query_content.visibility = View.VISIBLE
                    } else {
                        bin_query_content.visibility = View.GONE
                    }

                    if (sop.verifyCard) {
                        verify_card_status_result.text = verifyCardStatus.toString()
                        verify_card_return_code_result.text = verifyCardReturnCode
                        verify_card_return_message_result.text = verifyCardReturnMessage

                        verify_card_content.visibility = View.VISIBLE
                    } else {
                        verify_card_content.visibility = View.GONE
                    }
                }

                validation_content.visibility = View.GONE
                error_content.visibility = View.GONE
                result_content.visibility = View.VISIBLE
            }
        }
    }
}