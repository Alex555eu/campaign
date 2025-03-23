
export namespace ApiPaths {
    export enum AuthenticatePaths {
        POST_AUTHENTICATE =  '/api/v1/auth/authenticate',
        POST_REGISTER = '/api/v1/auth/register',
        POST_REFRESH = '/api/v1/auth/refresh',
        POST_LOGOUT = '/api/v1/auth/logout',
    }

    export enum CampaignPaths {
        GET_ALL_USER_CAMPAIGNS = '/api/v1/campaign',
        POST_USER_CAMPAIGN = '/api/v1/campaign',
        PUT_USER_CAMPAIGN = '/api/v1/campaign',
        DELETE_USER_CAMPAIGN = '/api/v1/campaign'
    }

    export enum UserPaths {
        GET_USER = '/api/v1/user',
        // DELETE_USER_ACCOUNT = '/api/v1/user'
    }

    export enum KeywordPaths {
        GET_MATCHING_KEYWORDS = '/api/v1/keyword/matching?query='
    }

    export enum TownPaths {
        GET_ALL_TOWNS = '/api/v1/town/all'
    } 

    export enum BidPaths {
        BID_PRODUCT = '/api/v1/bid'
    }
    
}