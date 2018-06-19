package com.enecuum.androidapp.utils

import android.view.View
import android.widget.TextView
import com.enecuum.androidapp.R
import io.github.novacrypto.bip39.MnemonicGenerator
import io.github.novacrypto.bip39.MnemonicValidator
import io.github.novacrypto.bip39.Validation.InvalidChecksumException
import io.github.novacrypto.bip39.Validation.InvalidWordCountException
import io.github.novacrypto.bip39.Validation.UnexpectedWhiteSpaceException
import io.github.novacrypto.bip39.Validation.WordNotFoundException
import io.github.novacrypto.bip39.Words
import io.github.novacrypto.bip39.wordlists.English
import java.security.SecureRandom


/**
 * Created by oleg on 26.01.18.
 */
object SeedUtils {
    fun displayRemainingCount(size: Int, hintText: TextView) {
        if (size <= 0)
            hintText.visibility = View.INVISIBLE
        else
            hintText.visibility = View.VISIBLE
        hintText.text = String.format("%s %d", hintText.context.getString(R.string.words_left), size)
    }

    fun generateMnemonic(): String {
        val sb = StringBuilder()
        val entropy = ByteArray(Words.TWELVE.byteLength())
        SecureRandom().nextBytes(entropy)
        MnemonicGenerator(English.INSTANCE)
                .createMnemonic(entropy, { sb.append(it) })
        return sb.toString()
    }

    @Throws(UnexpectedWhiteSpaceException::class, InvalidWordCountException::class, InvalidChecksumException::class, WordNotFoundException::class)
    fun validateMnemonic(mnemonic: String) {
//        try {
        MnemonicValidator
                .ofWordList(English.INSTANCE)
                .validate(mnemonic);
//        } catch (UnexpectedWhiteSpaceException e) {
//            ...
//        } catch (InvalidWordCountException e) {
//            ...
//        } catch (InvalidChecksumException e) {
//            ...
//        } catch (WordNotFoundException e) {
//            ...
//            //e.getSuggestion1()
//            //e.getSuggestion2()
//        }
    }
}