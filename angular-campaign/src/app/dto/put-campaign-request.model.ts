export interface PutCampaignRequest {
    campaignId: string,
    campaignName: string,
    keywordIds: string[],
    bidAmount: number,
    campaignFund: number,
    productUrl: string,
    status: boolean,
    townId: string,
    radius: number
}