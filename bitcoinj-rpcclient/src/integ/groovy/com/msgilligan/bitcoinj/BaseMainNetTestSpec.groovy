package com.msgilligan.bitcoinj

import com.msgilligan.bitcoinj.rpc.BitcoinExtendedClient
import com.msgilligan.bitcoinj.rpc.Loggable
import com.msgilligan.bitcoinj.rpc.RPCURI
import com.msgilligan.bitcoinj.rpc.test.TestServers
import com.msgilligan.bitcoinj.test.BTCTestSupport
import com.msgilligan.bitcoinj.test.RegTestFundingSource
import org.bitcoinj.core.Coin
import spock.lang.Specification

/**
 * Abstract Base class for Spock tests of Bitcoin Core on MainNet
 */
abstract class BaseMainNetTestSpec extends Specification implements BTCTestSupport, Loggable {
    static final private TestServers testServers = TestServers.instance
    static final protected String rpcTestUser = testServers.rpcTestUser
    static final protected String rpcTestPassword = testServers.rpcTestPassword;

    // Initializer to set up trait properties, Since Spock doesn't allow constructors
    {
        client = new BitcoinExtendedClient(RPCURI.defaultMainNetURI, rpcTestUser, rpcTestPassword)
        fundingSource = null    // No funding source implementation for MainNet yet.
    }

    void setupSpec() {
        Boolean available = client.waitForServer(60)   // Wait up to 1 minute
        if (!available) {
            log.error "Timeout error."
        }
        assert available
    }

    /**
     * Clean up after all tests in spec have run.
     */
    void cleanupSpec() {
    }

}