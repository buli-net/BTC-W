package com.example.btcwallet
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.wallet.DeterministicSeed
import org.bitcoinj.wallet.Wallet
import org.bitcoinj.crypto.MnemonicCode
import java.security.MessageDigest
import java.security.SecureRandom

object WalletManager {
    private fun prefs(c: Context) = EncryptedSharedPreferences.create(
        c, "btc_wallet", MasterKey.Builder(c).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private fun hash(p: String) = MessageDigest.getInstance("SHA-256").digest(p.toByteArray()).joinToString(""){"%02x".format(it)}
    fun hasWallet(c: Context) = prefs(c).contains("seed")
    fun verifyPassword(c: Context, p: String) = prefs(c).getString("pass","") == hash(p)
    fun createWallet(c: Context, pw: String): List<String> {
        val e = ByteArray(16).also{SecureRandom().nextBytes(it)}
        val m = MnemonicCode.INSTANCE.toMnemonic(e)
        prefs(c).edit().putString("seed", m.joinToString(" ")).putString("pass", hash(pw)).apply()
        return m
    }
    fun importWallet(c: Context, s: String, pw: String) = try {
        val m = s.trim().split(" "); MnemonicCode.INSTANCE.check(m)
        prefs(c).edit().putString("seed", s.trim()).putString("pass", hash(pw)).apply(); true
    } catch (_:Exception){ false }
    fun getMnemonic(c: Context, pw: String) = if(verifyPassword(c,pw)) prefs(c).getString("seed","")?.split(" ") else null
    fun getAddress(c: Context, pw: String): String? {
        val m = getMnemonic(c,pw) ?: return null
        val seed = DeterministicSeed(m, null, "", System.currentTimeMillis()/1000)
        return Wallet.fromSeed(MainNetParams.get(), seed).currentReceiveAddress().toString()
    }
    fun deleteWallet(c: Context) = prefs(c).edit().clear().apply()
}