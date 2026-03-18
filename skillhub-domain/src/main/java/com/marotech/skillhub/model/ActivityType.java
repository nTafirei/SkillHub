package com.marotech.skillhub.model;

public enum ActivityType {
    AGENT_PURCHASE_ONLINE_OK("Agent Purchase Online Successful", HasAmount.YES),
    AGENT_PURCHASE_ONLINE_NOT_OK("Agent Purchase Online Failed", HasAmount.YES),
    USER_PURCHASE_ONLINE_OK("User Purchase Online Successful", HasAmount.YES),
    USER_PURCHASE_ONLINE_NOT_OK("User Purchase Online Failed", HasAmount.YES),
    ADDED_CHARGE_RATE("Added Charge Rate", HasAmount.YES),
    TAX_PAID("Tax Paid", HasAmount.YES),
    DISABLED_VENDOR("Disabled Vendor", HasAmount.NO),
    ENABLED_VENDOR("Enabled Vendor", HasAmount.NO),
    DELETED_ATTACHMENT("Delete attachment", HasAmount.NO),
    ADDED_ATTACHMENT("Added attachment", HasAmount.NO),
    DISABLED_CONTRACT("Disabled Contract", HasAmount.NO),
    ENABLED_CONTRACT("Enabled Contract", HasAmount.NO),
    EDITED_CONTRACT("Edited Contract", HasAmount.NO),
    ADDED_CONTRACT("Added Contract", HasAmount.NO),
    TOP_UP_AGENT_FLOAT("Top Up Agent Float", HasAmount.YES),
    TOPPED_UP_VENDOR_ACCOUNT("Topped Up Vendor Account", HasAmount.YES),
    TOPPED_UP_USER_ACCOUNT("Topped Up User Account", HasAmount.YES),
    COLLECT_AGENT_FLOAT("Collect Agent Float", HasAmount.YES),
    EXPORTED_MERCHANT_SALES_REPORT("Exported Merchant Sales Report", HasAmount.NO),
    EXPORTED_VENDOR_SALES_REPORT("Exported Vendor Sales Report", HasAmount.NO),
    ADDED_AGGREGATOR_COMMISSION("Added Aggregator Commission", HasAmount.YES),
    PAID_AGGREGATOR_COMMISSION("Paid Aggregator Commission", HasAmount.YES),
    RECEIVED_AGGREGATOR_COMMISSION("Received Aggregator Commission", HasAmount.YES),
    EDITED_CHARGE_RATE("Edited Charge Rate", HasAmount.YES),
    DISABLED_CHARGE_RATE("Disabled Charge Rate", HasAmount.YES),
    DISABLED_USER("Disabled User", HasAmount.NO),
    ENABLED_USER("Enabled User", HasAmount.NO),
    UPLOADED_ID("Uploaded Id", HasAmount.NO),
    VERIFIED_USER("Verified User", HasAmount.NO),
    PURCHASE_VIA_AGENT_OK("Purchase via Agent Successful", HasAmount.YES),
    PURCHASE_VIA_AGENT_NOT_OK("Purchase via Agent Failed", HasAmount.YES),
    ADDED_VENDOR_COMMISSION("Added Vendor Commission", HasAmount.YES),
    PAID_AGENT_COMMISSION("Paid Agent Commission", HasAmount.YES),
    RECEIVED_AGENT_COMMISSION("Received Agent Commission", HasAmount.YES),
    ADDED_AGENT_COMMISSION("Added Agent Commission", HasAmount.YES),
    PAID_VENDOR_COMMISSION("Paid Vendor Commission", HasAmount.YES),
    RECEIVED_VENDOR_COMMISSION("Received Vendor Commission", HasAmount.YES),
    EDITED_EXCHANGE_RATE("Edited Exchange Rate", HasAmount.YES),
    ADDED_PRODUCT("Added Product", HasAmount.NO),
    ENABLED_PRODUCT("Enabled Product", HasAmount.NO),
    DISABLED_PRODUCT("Disabled Product", HasAmount.NO),
    DEACTIVATED_AGENT("Deactivated Agent", HasAmount.NO),
    ACTIVATED_AGENT("Activated Agent", HasAmount.NO),
    EDITED_PRODUCT("Edited Product", HasAmount.NO),
    ADDED_MERCHANT("Added Merchant", HasAmount.NO),
    ENABLED_MERCHANT("Enabled Merchant", HasAmount.NO),
    DISABLED_MERCHANT("Disabled Merchant", HasAmount.NO),
    EDITED_MERCHANT("Edited Merchant", HasAmount.NO),
    EDITED_USER("Edited User", HasAmount.NO),
    ADDED_VENDOR("Added Vendor", HasAmount.NO),
    EDITED_VENDOR("Edited Vendor", HasAmount.NO),
    ASSIGNED_USER_ROLE("Assigned User Role", HasAmount.NO),
    REDEEMED_LOYALTY_POINTS("Redeemed Loyalty Points", HasAmount.YES),
    REMOVED_USER_ROLE("Removed User Role", HasAmount.NO),
    LOGGED_IN_ONLINE("Logged in Online", HasAmount.NO),
    LOGGED_IN_VIA_WEB_SERVICES("Logged in via Web Services", HasAmount.NO),
    REQUESTED_PASSWORD_RESET("Requested Password Reset", HasAmount.NO),
    RESET_PASSWORD("Reset Password", HasAmount.NO),
    BALANCE_INQUIRY("Balance Inquiry", HasAmount.YES),
    NEW_REGISTRATION_BY_USSD("New Registration by USSD", HasAmount.NO),
    NEW_REGISTRATION_ONLINE("New Registration Online", HasAmount.NO),
    NEW_REGISTRATION_BY_MOBILE("New Registration by Mobile", HasAmount.NO),
    PURCHASE_VIA_USSD_OK("Purchase via USSD Successful", HasAmount.YES),
    PURCHASE_VIA_USSD_NOT_OK("Purchase via USSD Failed", HasAmount.YES),
    ADDED_TO_CART("Added to Cart", HasAmount.YES),
    WALLET_PURCHASE_VIA_MOBILE_OK("Wallet Purchase via Mobile Successful", HasAmount.YES),
    WALLET_PURCHASE_VIA_MOBILE_NOT_OK("Wallet Purchase via Mobile Failed", HasAmount.YES),
    EXTERNAL_WALLET_PURCHASE_VIA_MOBILE_OK("External Wallet Purchase via Mobile Successful", HasAmount.YES),
    EXTERNAL_WALLET_PURCHASE_VIA_MOBILE_NOT_OK("External Wallet Purchase via Mobile Failed", HasAmount.YES);

    private final String label;
    private HasAmount hasAmount;
    ActivityType(String label, HasAmount hasAmount) {
        this.label = label;
        this.hasAmount = hasAmount;
    }

    public String getLabel() {
        return label;
    }

    public HasAmount getHasAmount() {
        return hasAmount;
    }
    public boolean getActuallyHasAmount() {
        return hasAmount == HasAmount.YES;
    }
    public enum HasAmount {
        YES, NO
    }
}
