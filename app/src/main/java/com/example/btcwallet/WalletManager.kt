package com.example.btcwallet

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.wallet.DeterministicSeed
import org.bitcoinj.wallet.Wallet
import java.security.MessageDigest
import java.security.SecureRandom

object WalletManager {
    private const val PREFS = "btc_wallet"
    private const val KEY_SEED = "seed"
    private const val KEY_PASS = "pass"

    private fun prefs(c: Context): EncryptedSharedPreferences {
        val key = MasterKey.Builder(c).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        return EncryptedSharedPreferences.create(
            c, PREFS, key,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    private fun hash(s: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(s.toByteArray())
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    fun hasWallet(c: Context): Boolean {
        return prefs(c).contains(KEY_SEED)
    }

    fun verifyPassword(c: Context, p: String): Boolean {
        val saved = prefs(c).getString(KEY_PASS, null)
        return saved != null && saved == hash(p)
    }

    fun createWallet(c: Context, p: String): List<String> {
        val entropy = ByteArray(16)
        SecureRandom().nextBytes(entropy)
        val mnemonic = MnemonicCode.INSTANCE.toMnemonic(entropy)
        prefs(c).edit()
            .putString(KEY_SEED, mnemonic.joinToString(" "))
            .putString(KEY_PASS, hash(p))
            .apply()
        return mnemonic
    }

    fun importWallet(c: Context, phrase: String, p: String): Boolean {
        return try {
            val words = phrase.trim().split("\\s+".toRegex())
            MnemonicCode.INSTANCE.check(words)
            prefs(c).edit()
                .putString(KEY_SEED, phrase.trim())
                .putString(KEY_PASS, hash(p))
                .apply()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getMnemonic(c: Context, p: String): List<String>? {
        if (!verifyPassword(c, p)) return null
        val seed = prefs(c).getString(KEY_SEED, null) ?: return null
        return seed.split(" ")
    }

    fun getAddress(c: Context, p: String): String? {
        val m = getMnemonic(c, p) ?: return null
        val seed = DeterministicSeed(m, null, "", System.currentTimeMillis() / 1000L)
        val wallet = Wallet.fromSeed(MainNetParams.get(), seed)
        return wallet.currentReceiveAddress().toString()
    }

    fun deleteWallet(c: Context) {
        prefs(c).edit().clear().apply()
    }
}