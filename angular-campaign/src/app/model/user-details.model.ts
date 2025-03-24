import { EmeraldWallet } from "./emerald-wallet.model";

export interface UserDetails {
    id: string,
    firstName: string,
    emailAddress: string,
    emeraldWallet: EmeraldWallet
}