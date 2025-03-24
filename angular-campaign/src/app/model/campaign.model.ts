import { EmeraldWallet } from "./emerald-wallet.model";
import { Keyword } from "./keyword.model";
import { Town } from "./town.model";
import { UserDetails } from "./user-details.model";

export interface Campaign {
    id: string,
    campaignName: string,
    userDetails: UserDetails,
    keywords: Keyword[],
    bidAmount: number,
    campaignFund: number,
    productUrl: string,
    status: boolean
    town: Town,
    radius: number,
    emeraldWallet: EmeraldWallet
}