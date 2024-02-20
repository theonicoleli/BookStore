export interface Message {
    id?: number;
    friendshipId?: number;
    content: string;
    timestamp: Date;
    senderId?: number;
    receiverId?: number;
}
