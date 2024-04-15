package com.example.socialnetworkfx.domain;

import com.example.socialnetworkfx.domain.abstractDomain.Entity;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;

import java.time.LocalDateTime;

public class FriendshipRequest extends Entity<Tuple<Long,Long>> {
        private Long idSender;
        private Long idReceiver;
        private Long Status;
        LocalDateTime date;

        public FriendshipRequest(Long idSender, Long idReceiver, Long Status){
            this.setId(new Tuple<>(idSender,idReceiver));
            this.idSender = idSender;
            this.idReceiver = idReceiver;
            this.Status = Status;
            this.date = LocalDateTime.now();
        }

        public Long getIdSender(){
            return idSender;
        }

        public Long getIdReceiver(){
            return idReceiver;
        }

        public Long getStatus(){
            return Status;
        }

        public void setStatus(Long Status){
            this.Status = Status;
        }

        public void setDate(LocalDateTime date){
            this.date = date;
        }

        public LocalDateTime getDate(){
            return date;
        }

        @Override
        public String toString() {
            return "FriendshipRequest{" +
                    "idSender=" + this.getId().getLeft() +
                    ", idReceiver=" + this.getId().getRight() +
                    ", Status=" + Status +
                    '}';
        }
}
